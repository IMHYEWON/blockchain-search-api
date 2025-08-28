package com.blockchain.search.service;

import com.blockchain.search.dto.SearchRequest;
import com.blockchain.search.dto.SearchResponse;
import com.blockchain.search.model.Address;
import com.blockchain.search.model.Token;
import com.blockchain.search.model.Transaction;

import java.util.List;
import java.util.Map;

public interface SearchService {
    
    /**
     * 통합 검색 - 모든 타입의 데이터를 검색
     */
    SearchResponse search(SearchRequest request);
    
    /**
     * 트랜잭션 검색
     */
    List<Transaction> searchTransactions(String query, String network, int page, int size);
    
    /**
     * 주소 검색
     */
    List<Address> searchAddresses(String query, String network, int page, int size);
    
    /**
     * 토큰 검색
     */
    List<Token> searchTokens(String query, String network, int page, int size);
    
    /**
     * 자동완성 검색
     */
    List<Map<String, Object>> autocomplete(String query, String network, String type, int maxResults);
    
    /**
     * URL 방식으로 Elasticsearch 검색
     */
    Map<String, Object> searchByUrl(String query, String index, int size, String network);
    
    /**
     * Query DSL 방식으로 Elasticsearch 검색
     */
    Map<String, Object> searchByQuery(String index, Map<String, Object> query, int size);
    
    /**
     * 인덱스 상태 확인
     */
    Map<String, Object> getIndicesStatus();
}
