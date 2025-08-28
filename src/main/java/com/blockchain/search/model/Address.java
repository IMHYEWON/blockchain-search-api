package com.blockchain.search.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Document(indexName = "addresses")
public class Address {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Keyword, analyzer = "address_analyzer")
    private String address;
    
    @Field(type = FieldType.Keyword)
    private String network;
    
    @Field(type = FieldType.Keyword)
    private String balance;
    
    @Field(type = FieldType.Nested)
    private List<TokenBalance> tokenBalances;
    
    @Field(type = FieldType.Long)
    private Long txCount;
    
    @Field(type = FieldType.Date)
    private LocalDateTime lastUpdated;
    
    @Field(type = FieldType.Keyword)
    private String type; // EOA, Contract
    
    @Field(type = FieldType.Text)
    private String name; // ENS name or contract name
    
    @Field(type = FieldType.Boolean)
    private Boolean verified;
    
    // Nested class for token balances
    public static class TokenBalance {
        @Field(type = FieldType.Keyword)
        private String tokenAddress;
        
        @Field(type = FieldType.Keyword)
        private String tokenSymbol;
        
        @Field(type = FieldType.Keyword)
        private String balance;
        
        @Field(type = FieldType.Double)
        private Double price;
        
        @Field(type = FieldType.Double)
        private Double valueUsd;
        
        // Constructors
        public TokenBalance() {}
        
        public TokenBalance(String tokenAddress, String tokenSymbol, String balance) {
            this.tokenAddress = tokenAddress;
            this.tokenSymbol = tokenSymbol;
            this.balance = balance;
        }
        
        // Getters and Setters
        public String getTokenAddress() { return tokenAddress; }
        public void setTokenAddress(String tokenAddress) { this.tokenAddress = tokenAddress; }
        
        public String getTokenSymbol() { return tokenSymbol; }
        public void setTokenSymbol(String tokenSymbol) { this.tokenSymbol = tokenSymbol; }
        
        public String getBalance() { return balance; }
        public void setBalance(String balance) { this.balance = balance; }
        
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        
        public Double getValueUsd() { return valueUsd; }
        public void setValueUsd(Double valueUsd) { this.valueUsd = valueUsd; }
    }
    
    // Constructors
    public Address() {}
    
    public Address(String address, String network) {
        this.address = address;
        this.network = network;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }
    
    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }
    
    public List<TokenBalance> getTokenBalances() { return tokenBalances; }
    public void setTokenBalances(List<TokenBalance> tokenBalances) { this.tokenBalances = tokenBalances; }
    
    public Long getTxCount() { return txCount; }
    public void setTxCount(Long txCount) { this.txCount = txCount; }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    
    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", network='" + network + '\'' +
                ", balance='" + balance + '\'' +
                ", tokenBalances=" + tokenBalances +
                ", txCount=" + txCount +
                ", lastUpdated=" + lastUpdated +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", verified=" + verified +
                '}';
    }
}
