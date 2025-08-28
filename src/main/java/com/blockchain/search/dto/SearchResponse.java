package com.blockchain.search.dto;

import com.blockchain.search.model.Address;
import com.blockchain.search.model.Token;
import com.blockchain.search.model.Transaction;

import java.util.List;

public class SearchResponse {
    
    private boolean success;
    private String message;
    private SearchData data;
    private long searchTime;
    private String error;
    
    // Nested class for search data
    public static class SearchData {
        private String query;
        private String network;
        private List<Transaction> transactions;
        private List<Address> addresses;
        private List<Token> tokens;
        private long totalCount;
        private int page;
        private int size;
        
        // Constructors
        public SearchData() {}
        
        public SearchData(String query, String network) {
            this.query = query;
            this.network = network;
        }
        
        // Getters and Setters
        public String getQuery() { return query; }
        public void setQuery(String query) { this.query = query; }
        
        public String getNetwork() { return network; }
        public void setNetwork(String network) { this.network = network; }
        
        public List<Transaction> getTransactions() { return transactions; }
        public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
        
        public List<Address> getAddresses() { return addresses; }
        public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
        
        public List<Token> getTokens() { return tokens; }
        public void setTokens(List<Token> tokens) { this.tokens = tokens; }
        
        public long getTotalCount() { return totalCount; }
        public void setTotalCount(long totalCount) { this.totalCount = totalCount; }
        
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        
        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }
    }
    
    // Constructors
    public SearchResponse() {}
    
    public SearchResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static SearchResponse success(String message, SearchData data, long searchTime) {
        SearchResponse response = new SearchResponse();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setSearchTime(searchTime);
        return response;
    }
    
    public static SearchResponse error(String message, String error) {
        SearchResponse response = new SearchResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setError(error);
        return response;
    }
    
    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public SearchData getData() { return data; }
    public void setData(SearchData data) { this.data = data; }
    
    public long getSearchTime() { return searchTime; }
    public void setSearchTime(long searchTime) { this.searchTime = searchTime; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
