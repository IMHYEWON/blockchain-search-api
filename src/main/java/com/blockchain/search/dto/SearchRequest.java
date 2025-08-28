package com.blockchain.search.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SearchRequest {
    
    @NotBlank(message = "검색어는 필수입니다")
    @Size(min = 1, max = 100, message = "검색어는 1-100자 사이여야 합니다")
    private String query;
    
    private String network;
    
    private String type; // transaction, address, token, all
    
    private Integer page = 0;
    
    private Integer size = 20;
    
    private String sortBy = "relevance"; // relevance, timestamp, value
    
    private String sortOrder = "desc"; // asc, desc
    
    // Constructors
    public SearchRequest() {}
    
    public SearchRequest(String query) {
        this.query = query;
    }
    
    public SearchRequest(String query, String network) {
        this.query = query;
        this.network = network;
    }
    
    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    
    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }
    
    @Override
    public String toString() {
        return "SearchRequest{" +
                "query='" + query + '\'' +
                ", network='" + network + '\'' +
                ", type='" + type + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}
