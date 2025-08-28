package com.blockchain.search.service.impl;

import com.blockchain.search.dto.SearchRequest;
import com.blockchain.search.dto.SearchResponse;
import com.blockchain.search.model.Address;
import com.blockchain.search.model.Token;
import com.blockchain.search.model.Transaction;
import com.blockchain.search.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    
    @Autowired
    @Qualifier("customElasticsearchClient")
    private RestHighLevelClient elasticsearchClient;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public SearchResponse search(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            SearchResponse.SearchData searchData = new SearchResponse.SearchData(
                    request.getQuery(), request.getNetwork());
            
            // 검색 타입에 따라 다른 검색 수행
            String type = request.getType();
            if ("transaction".equals(type) || "all".equals(type)) {
                List<Transaction> transactions = searchTransactions(
                        request.getQuery(), request.getNetwork(), request.getPage(), request.getSize());
                searchData.setTransactions(transactions);
            }
            
            if ("address".equals(type) || "all".equals(type)) {
                List<Address> addresses = searchAddresses(
                        request.getQuery(), request.getNetwork(), request.getPage(), request.getSize());
                searchData.setAddresses(addresses);
            }
            
            if ("token".equals(type) || "all".equals(type)) {
                List<Token> tokens = searchTokens(
                        request.getQuery(), request.getNetwork(), request.getPage(), request.getSize());
                searchData.setTokens(tokens);
            }
            
            // 총 개수 계산
            long totalCount = (searchData.getTransactions() != null ? searchData.getTransactions().size() : 0) +
                            (searchData.getAddresses() != null ? searchData.getAddresses().size() : 0) +
                            (searchData.getTokens() != null ? searchData.getTokens().size() : 0);
            searchData.setTotalCount(totalCount);
            searchData.setPage(request.getPage());
            searchData.setSize(request.getSize());
            
            long searchTime = System.currentTimeMillis() - startTime;
            
            return SearchResponse.success("검색이 완료되었습니다", searchData, searchTime);
            
        } catch (Exception e) {
            return SearchResponse.error("검색 중 오류가 발생했습니다", e.getMessage());
        }
    }
    
    @Override
    public List<Transaction> searchTransactions(String query, String network, int page, int size) {
        try {
            org.elasticsearch.action.search.SearchRequest searchRequest = new org.elasticsearch.action.search.SearchRequest("transactions");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            
            // 쿼리 구성
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            
            // 검색어가 해시인지 확인
            if (isHash(query)) {
                boolQuery.should(QueryBuilders.wildcardQuery("txHash", "*" + query + "*"));
                boolQuery.should(QueryBuilders.wildcardQuery("fromAddress", "*" + query + "*"));
                boolQuery.should(QueryBuilders.wildcardQuery("toAddress", "*" + query + "*"));
            } else {
                boolQuery.should(QueryBuilders.multiMatchQuery(query, "txHash", "fromAddress", "toAddress")
                        .fuzziness("AUTO"));
            }
            
            // 네트워크 필터
            if (StringUtils.hasText(network)) {
                boolQuery.filter(QueryBuilders.termQuery("network", network));
            }
            
            sourceBuilder.query(boolQuery);
            sourceBuilder.from(page * size);
            sourceBuilder.size(size);
            sourceBuilder.sort("timestamp", SortOrder.DESC);
            
            searchRequest.source(sourceBuilder);
            
            org.elasticsearch.action.search.SearchResponse response = 
                    elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            
            return Arrays.stream(response.getHits().getHits())
                    .map(hit -> objectMapper.convertValue(hit.getSourceAsMap(), Transaction.class))
                    .collect(Collectors.toList());
                    
        } catch (IOException e) {
            throw new RuntimeException("트랜잭션 검색 중 오류 발생", e);
        }
    }
    
    @Override
    public List<Address> searchAddresses(String query, String network, int page, int size) {
        try {
            org.elasticsearch.action.search.SearchRequest searchRequest = new org.elasticsearch.action.search.SearchRequest("addresses");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            
            if (isHash(query)) {
                boolQuery.should(QueryBuilders.wildcardQuery("address", "*" + query + "*"));
            } else {
                boolQuery.should(QueryBuilders.multiMatchQuery(query, "address", "name")
                        .fuzziness("AUTO"));
            }
            
            if (StringUtils.hasText(network)) {
                boolQuery.filter(QueryBuilders.termQuery("network", network));
            }
            
            sourceBuilder.query(boolQuery);
            sourceBuilder.from(page * size);
            sourceBuilder.size(size);
            sourceBuilder.sort("txCount", SortOrder.DESC);
            
            searchRequest.source(sourceBuilder);
            
            org.elasticsearch.action.search.SearchResponse response = 
                    elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            
            return Arrays.stream(response.getHits().getHits())
                    .map(hit -> objectMapper.convertValue(hit.getSourceAsMap(), Address.class))
                    .collect(Collectors.toList());
                    
        } catch (IOException e) {
            throw new RuntimeException("주소 검색 중 오류 발생", e);
        }
    }
    
    @Override
    public List<Token> searchTokens(String query, String network, int page, int size) {
        try {
            org.elasticsearch.action.search.SearchRequest searchRequest = new org.elasticsearch.action.search.SearchRequest("tokens");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            
            if (isHash(query)) {
                boolQuery.should(QueryBuilders.wildcardQuery("tokenAddress", "*" + query + "*"));
            } else {
                boolQuery.should(QueryBuilders.multiMatchQuery(query, "symbol", "name")
                        .fuzziness("AUTO"));
            }
            
            if (StringUtils.hasText(network)) {
                boolQuery.filter(QueryBuilders.termQuery("network", network));
            }
            
            sourceBuilder.query(boolQuery);
            sourceBuilder.from(page * size);
            sourceBuilder.size(size);
            sourceBuilder.sort("holders", SortOrder.DESC);
            
            searchRequest.source(sourceBuilder);
            
            org.elasticsearch.action.search.SearchResponse response = 
                    elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            
            return Arrays.stream(response.getHits().getHits())
                    .map(hit -> objectMapper.convertValue(hit.getSourceAsMap(), Token.class))
                    .collect(Collectors.toList());
                    
        } catch (IOException e) {
            throw new RuntimeException("토큰 검색 중 오류 발생", e);
        }
    }
    
    @Override
    public List<Map<String, Object>> autocomplete(String query, String network, String type, int maxResults) {
        try {
            List<Map<String, Object>> results = new ArrayList<>();
            
            if ("token".equals(type) || type == null) {
                List<Token> tokens = searchTokens(query, network, 0, maxResults);
                for (Token token : tokens) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("type", "token");
                    result.put("symbol", token.getSymbol());
                    result.put("name", token.getName());
                    result.put("network", token.getNetwork());
                    result.put("tokenAddress", token.getTokenAddress());
                    results.add(result);
                }
            }
            
            if ("address".equals(type) || type == null) {
                List<Address> addresses = searchAddresses(query, network, 0, maxResults);
                for (Address address : addresses) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("type", "address");
                    result.put("address", address.getAddress());
                    result.put("network", address.getNetwork());
                    result.put("name", address.getName());
                    results.add(result);
                }
            }
            
            if ("transaction".equals(type) || type == null) {
                List<Transaction> transactions = searchTransactions(query, network, 0, maxResults);
                for (Transaction tx : transactions) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("type", "transaction");
                    result.put("txHash", tx.getTxHash());
                    result.put("network", tx.getNetwork());
                    result.put("fromAddress", tx.getFromAddress());
                    result.put("toAddress", tx.getToAddress());
                    results.add(result);
                }
            }
            
            // 결과 수 제한
            return results.stream().limit(maxResults).collect(Collectors.toList());
            
        } catch (Exception e) {
            throw new RuntimeException("자동완성 검색 중 오류 발생", e);
        }
    }
    
    @Override
    public Map<String, Object> searchByUrl(String query, String index, int size, String network) {
        try {
            org.elasticsearch.action.search.SearchRequest searchRequest = new org.elasticsearch.action.search.SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            
            // 간단한 match 쿼리
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.should(QueryBuilders.multiMatchQuery(query).field("*"));
            
            if (StringUtils.hasText(network)) {
                boolQuery.filter(QueryBuilders.termQuery("network", network));
            }
            
            sourceBuilder.query(boolQuery);
            sourceBuilder.size(size);
            
            searchRequest.source(sourceBuilder);
            
            org.elasticsearch.action.search.SearchResponse response = 
                    elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            
            return convertSearchResponseToMap(response);
            
        } catch (IOException e) {
            throw new RuntimeException("URL 검색 중 오류 발생", e);
        }
    }
    
    @Override
    public Map<String, Object> searchByQuery(String index, Map<String, Object> queryMap, int size) {
        try {
            org.elasticsearch.action.search.SearchRequest searchRequest = new org.elasticsearch.action.search.SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            
            // Query DSL을 SearchSourceBuilder로 변환
            if (queryMap.containsKey("query")) {
                sourceBuilder.query(QueryBuilders.wrapperQuery(objectMapper.writeValueAsString(queryMap.get("query"))));
            }
            
            if (queryMap.containsKey("sort")) {
                // 정렬 로직 구현
            }
            
            sourceBuilder.size(size);
            
            searchRequest.source(sourceBuilder);
            
            org.elasticsearch.action.search.SearchResponse response = 
                    elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            
            return convertSearchResponseToMap(response);
            
        } catch (Exception e) {
            throw new RuntimeException("Query DSL 검색 중 오류 발생", e);
        }
    }
    
    @Override
    public Map<String, Object> getIndicesStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            
            // 인덱스별 문서 수 확인
            String[] indices = {"transactions", "addresses", "tokens"};
            
            for (String index : indices) {
                org.elasticsearch.action.search.SearchRequest searchRequest = new org.elasticsearch.action.search.SearchRequest(index);
                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
                sourceBuilder.size(0); // 문서 내용은 가져오지 않음
                
                searchRequest.source(sourceBuilder);
                
                org.elasticsearch.action.search.SearchResponse response = 
                        elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
                
                Map<String, Object> indexStatus = new HashMap<>();
                indexStatus.put("documentCount", response.getHits().getTotalHits().value);
                indexStatus.put("status", "active");
                
                status.put(index, indexStatus);
            }
            
            return status;
            
        } catch (IOException e) {
            throw new RuntimeException("인덱스 상태 확인 중 오류 발생", e);
        }
    }
    
    // 유틸리티 메서드
    private boolean isHash(String query) {
        return query != null && query.matches("^0x[a-fA-F0-9]+$");
    }
    
    private Map<String, Object> convertSearchResponseToMap(org.elasticsearch.action.search.SearchResponse response) {
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> hits = Arrays.stream(response.getHits().getHits())
                .map(SearchHit::getSourceAsMap)
                .collect(Collectors.toList());
        
        result.put("total", response.getHits().getTotalHits().value);
        result.put("hits", hits);
        result.put("took", response.getTook().getMillis());
        
        return result;
    }
}
