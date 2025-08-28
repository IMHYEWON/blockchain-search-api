package com.blockchain.search.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(indexName = "transactions")
public class Transaction {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Keyword, analyzer = "hash_analyzer")
    private String txHash;
    
    @Field(type = FieldType.Keyword)
    private String network;
    
    @Field(type = FieldType.Keyword, analyzer = "address_analyzer")
    private String fromAddress;
    
    @Field(type = FieldType.Keyword, analyzer = "address_analyzer")
    private String toAddress;
    
    @Field(type = FieldType.Long)
    private Long blockNumber;
    
    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    @Field(type = FieldType.Keyword)
    private String value;
    
    @Field(type = FieldType.Keyword)
    private String tokenSymbol;
    
    @Field(type = FieldType.Keyword)
    private String tokenAddress;
    
    @Field(type = FieldType.Long)
    private Long gasUsed;
    
    @Field(type = FieldType.Keyword)
    private String gasPrice;
    
    @Field(type = FieldType.Keyword)
    private String status;
    
    @Field(type = FieldType.Text)
    private String inputData;
    
    // Constructors
    public Transaction() {}
    
    public Transaction(String txHash, String network, String fromAddress, String toAddress) {
        this.txHash = txHash;
        this.network = network;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTxHash() { return txHash; }
    public void setTxHash(String txHash) { this.txHash = txHash; }
    
    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }
    
    public String getFromAddress() { return fromAddress; }
    public void setFromAddress(String fromAddress) { this.fromAddress = fromAddress; }
    
    public String getToAddress() { return toAddress; }
    public void setToAddress(String toAddress) { this.toAddress = toAddress; }
    
    public Long getBlockNumber() { return blockNumber; }
    public void setBlockNumber(Long blockNumber) { this.blockNumber = blockNumber; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public String getTokenSymbol() { return tokenSymbol; }
    public void setTokenSymbol(String tokenSymbol) { this.tokenSymbol = tokenSymbol; }
    
    public String getTokenAddress() { return tokenAddress; }
    public void setTokenAddress(String tokenAddress) { this.tokenAddress = tokenAddress; }
    
    public Long getGasUsed() { return gasUsed; }
    public void setGasUsed(Long gasUsed) { this.gasUsed = gasUsed; }
    
    public String getGasPrice() { return gasPrice; }
    public void setGasPrice(String gasPrice) { this.gasPrice = gasPrice; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", txHash='" + txHash + '\'' +
                ", network='" + network + '\'' +
                ", fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", blockNumber=" + blockNumber +
                ", timestamp=" + timestamp +
                ", value='" + value + '\'' +
                ", tokenSymbol='" + tokenSymbol + '\'' +
                ", tokenAddress='" + tokenAddress + '\'' +
                ", gasUsed=" + gasUsed +
                ", gasPrice='" + gasPrice + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
