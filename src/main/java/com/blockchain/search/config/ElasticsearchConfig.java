package com.blockchain.search.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.blockchain.search.repository")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
    
    @Value("${elasticsearch.host:localhost}")
    private String host;
    
    @Value("${elasticsearch.port:9200}")
    private int port;
    
    @Value("${elasticsearch.scheme:http}")
    private String scheme;
    
    @Value("${elasticsearch.username:}")
    private String username;
    
    @Value("${elasticsearch.password:}")
    private String password;
    
    @Value("${elasticsearch.connection-timeout:5000}")
    private int connectionTimeout;
    
    @Value("${elasticsearch.read-timeout:10000}")
    private int readTimeout;
    
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .withConnectTimeout(connectionTimeout)
                .withSocketTimeout(readTimeout)
                .build();
        
        return RestClients.create(clientConfiguration).rest();
    }
    
    @Bean
    public RestHighLevelClient customElasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(host, port, scheme)
        );
        
        // Add authentication if credentials are provided
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, 
                    new UsernamePasswordCredentials(username, password));
            
            builder.setHttpClientConfigCallback(httpClientBuilder -> 
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }
        
        // Add request timeout configuration
        builder.setRequestConfigCallback(requestConfigBuilder -> 
                requestConfigBuilder
                        .setConnectTimeout(connectionTimeout)
                        .setSocketTimeout(readTimeout));
        
        return new RestHighLevelClient(builder);
    }
}
