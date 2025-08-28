package com.blockchain.search.controller;

import com.blockchain.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/es")
@Validated
@CrossOrigin(origins = "*")
public class ElasticsearchController {
    
    @Autowired
    private SearchService searchService;
    
    /**
     * URL 방식으로 Elasticsearch 검색
     * GET /api/v1/es/url-search?q={query}&index={index}&size={size}&network={network}
     */
    @GetMapping("/url-search")
    public ResponseEntity<Map<String, Object>> searchByUrl(
            @RequestParam @NotBlank(message = "검색어는 필수입니다") String q,
            @RequestParam(defaultValue = "transactions") String index,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size,
            @RequestParam(required = false) String network) {
        
        try {
            Map<String, Object> result = searchService.searchByUrl(q, index, size, network);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "URL 검색 중 오류가 발생했습니다");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Query DSL 방식으로 Elasticsearch 검색
     * POST /api/v1/es/query-search
     */
    @PostMapping("/query-search")
    public ResponseEntity<Map<String, Object>> searchByQuery(
            @RequestBody Map<String, Object> requestBody) {
        
        try {
            String index = (String) requestBody.getOrDefault("index", "transactions");
            Integer size = (Integer) requestBody.getOrDefault("size", 20);
            
            if (size == null || size < 1 || size > 100) {
                size = 20;
            }
            
            Map<String, Object> result = searchService.searchByQuery(index, requestBody, size);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Query DSL 검색 중 오류가 발생했습니다");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 인덱스 상태 확인
     * GET /api/v1/es/indices
     */
    @GetMapping("/indices")
    public ResponseEntity<Map<String, Object>> getIndicesStatus() {
        try {
            Map<String, Object> status = searchService.getIndicesStatus();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "인덱스 상태 확인 중 오류가 발생했습니다");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 특정 인덱스의 문서 수 확인
     * GET /api/v1/es/indices/{indexName}/count
     */
    @GetMapping("/indices/{indexName}/count")
    public ResponseEntity<Map<String, Object>> getIndexDocumentCount(
            @PathVariable String indexName) {
        
        try {
            Map<String, Object> status = searchService.getIndicesStatus();
            if (status.containsKey(indexName)) {
                Map<String, Object> indexStatus = (Map<String, Object>) status.get(indexName);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("index", indexName);
                response.put("documentCount", indexStatus.get("documentCount"));
                response.put("status", indexStatus.get("status"));
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "인덱스 문서 수 확인 중 오류가 발생했습니다");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 인덱스별 통계 정보
     * GET /api/v1/es/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getElasticsearchStats() {
        try {
            Map<String, Object> indicesStatus = searchService.getIndicesStatus();
            
            // 전체 통계 계산
            long totalDocuments = indicesStatus.values().stream()
                    .mapToLong(status -> {
                        Map<String, Object> indexStatus = (Map<String, Object>) status;
                        return (Long) indexStatus.get("documentCount");
                    })
                    .sum();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalIndices", indicesStatus.size());
            stats.put("totalDocuments", totalDocuments);
            stats.put("indices", indicesStatus);
            stats.put("timestamp", System.currentTimeMillis());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "통계 정보 조회 중 오류가 발생했습니다");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
