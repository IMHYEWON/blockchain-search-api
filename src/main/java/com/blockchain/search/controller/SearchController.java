package com.blockchain.search.controller;

import com.blockchain.search.dto.SearchRequest;
import com.blockchain.search.dto.SearchResponse;
import com.blockchain.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Validated
@CrossOrigin(origins = "*")
public class SearchController {
    
    @Autowired
    private SearchService searchService;
    
    @Value("${search.autocomplete.max-results:10}")
    private int maxAutocompleteResults;
    
    /**
     * 통합 검색 API
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam @NotBlank(message = "검색어는 필수입니다") String q,
            @RequestParam(required = false) String network,
            @RequestParam(required = false, defaultValue = "all") String type,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestParam(defaultValue = "relevance") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        
        SearchRequest request = new SearchRequest();
        request.setQuery(q);
        request.setNetwork(network);
        request.setType(type);
        request.setPage(page);
        request.setSize(size);
        request.setSortBy(sortBy);
        request.setSortOrder(sortOrder);
        
        SearchResponse response = searchService.search(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 자동완성 API
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<Map<String, Object>> autocomplete(
            @RequestParam @NotBlank(message = "검색어는 필수입니다") String q,
            @RequestParam(required = false) String network,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer maxResults) {
        
        List<Map<String, Object>> results = searchService.autocomplete(q, network, type, 
                Math.min(maxResults, maxAutocompleteResults));
        
        Map<String, Object> response = Map.of(
                "success", true,
                "query", q,
                "network", network,
                "type", type,
                "results", results,
                "totalCount", results.size()
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 트랜잭션 검색 API
     */
    @GetMapping("/transactions")
    public ResponseEntity<Map<String, Object>> searchTransactions(
            @RequestParam(required = false) String hash,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String network,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        
        String query = hash != null ? hash : (from != null ? from : to);
        if (query == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "hash, from, 또는 to 파라미터 중 하나는 필수입니다"
            ));
        }
        
        List<Map<String, Object>> results = searchService.searchTransactions(query, network, page, size)
                .stream()
                .map(tx -> Map.of(
                        "txHash", tx.getTxHash(),
                        "network", tx.getNetwork(),
                        "fromAddress", tx.getFromAddress(),
                        "toAddress", tx.getToAddress(),
                        "blockNumber", tx.getBlockNumber(),
                        "timestamp", tx.getTimestamp(),
                        "value", tx.getValue(),
                        "tokenSymbol", tx.getTokenSymbol(),
                        "gasUsed", tx.getGasUsed()
                ))
                .toList();
        
        Map<String, Object> response = Map.of(
                "success", true,
                "query", query,
                "network", network,
                "results", results,
                "totalCount", results.size(),
                "page", page,
                "size", size
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 주소 검색 API
     */
    @GetMapping("/addresses/{address}")
    public ResponseEntity<Map<String, Object>> searchAddress(
            @PathVariable @NotBlank(message = "주소는 필수입니다") String address,
            @RequestParam(required = false) String network) {
        
        List<Map<String, Object>> results = searchService.searchAddresses(address, network, 0, 1)
                .stream()
                .map(addr -> Map.of(
                        "address", addr.getAddress(),
                        "network", addr.getNetwork(),
                        "balance", addr.getBalance(),
                        "tokenBalances", addr.getTokenBalances(),
                        "txCount", addr.getTxCount(),
                        "lastUpdated", addr.getLastUpdated(),
                        "type", addr.getType(),
                        "name", addr.getName(),
                        "verified", addr.getVerified()
                ))
                .toList();
        
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> response = Map.of(
                "success", true,
                "address", address,
                "network", network,
                "data", results.get(0)
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 토큰 검색 API
     */
    @GetMapping("/tokens")
    public ResponseEntity<Map<String, Object>> searchTokens(
            @RequestParam(required = false) String symbol,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String network,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size) {
        
        String query = symbol != null ? symbol : name;
        if (query == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "symbol 또는 name 파라미터 중 하나는 필수입니다"
            ));
        }
        
        List<Map<String, Object>> results = searchService.searchTokens(query, network, page, size)
                .stream()
                .map(token -> Map.of(
                        "tokenAddress", token.getTokenAddress(),
                        "network", token.getNetwork(),
                        "symbol", token.getSymbol(),
                        "name", token.getName(),
                        "decimals", token.getDecimals(),
                        "totalSupply", token.getTotalSupply(),
                        "holders", token.getHolders(),
                        "marketCap", token.getMarketCap(),
                        "price", token.getPrice(),
                        "contractType", token.getContractType(),
                        "verified", token.getVerified()
                ))
                .toList();
        
        Map<String, Object> response = Map.of(
                "success", true,
                "query", query,
                "network", network,
                "results", results,
                "totalCount", results.size(),
                "page", page,
                "size", size
        );
        
        return ResponseEntity.ok(response);
    }
}
