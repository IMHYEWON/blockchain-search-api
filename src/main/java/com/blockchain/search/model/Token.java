package com.blockchain.search.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Document(indexName = "tokens")
public class Token {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Keyword)
    private String tokenAddress;
    
    @Field(type = FieldType.Keyword)
    private String network;
    
    @Field(type = FieldType.Text, analyzer = "token_analyzer", searchAnalyzer = "token_search_analyzer")
    private String symbol;
    
    @Field(type = FieldType.Text, analyzer = "token_analyzer")
    private String name;
    
    @Field(type = FieldType.Integer)
    private Integer decimals;
    
    @Field(type = FieldType.Keyword)
    private String totalSupply;
    
    @Field(type = FieldType.Long)
    private Long holders;
    
    @Field(type = FieldType.Double)
    private Double marketCap;
    
    @Field(type = FieldType.Double)
    private Double price;
    
    @Field(type = FieldType.Keyword)
    private String contractType; // ERC20, ERC721, etc.
    
    @Field(type = FieldType.Boolean)
    private Boolean verified;
    
    @Field(type = FieldType.Keyword)
    private String website;
    
    @Field(type = FieldType.Keyword)
    private String description;
    
    // Constructors
    public Token() {}
    
    public Token(String tokenAddress, String network, String symbol, String name) {
        this.tokenAddress = tokenAddress;
        this.network = network;
        this.symbol = symbol;
        this.name = name;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTokenAddress() { return tokenAddress; }
    public void setTokenAddress(String tokenAddress) { this.tokenAddress = tokenAddress; }
    
    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }
    
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getDecimals() { return decimals; }
    public void setDecimals(Integer decimals) { this.decimals = decimals; }
    
    public String getTotalSupply() { return totalSupply; }
    public void setTotalSupply(String totalSupply) { this.totalSupply = totalSupply; }
    
    public Long getHolders() { return holders; }
    public void setHolders(Long holders) { this.holders = holders; }
    
    public Double getMarketCap() { return marketCap; }
    public void setMarketCap(Double marketCap) { this.marketCap = marketCap; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return "Token{" +
                "id='" + id + '\'' +
                ", tokenAddress='" + tokenAddress + '\'' +
                ", network='" + network + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", decimals=" + decimals +
                ", totalSupply='" + totalSupply + '\'' +
                ", holders=" + holders +
                ", marketCap=" + marketCap +
                ", price=" + price +
                ", contractType='" + contractType + '\'' +
                ", verified=" + verified +
                '}';
    }
}
