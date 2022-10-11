package com.luyc.esclient.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * @author luyc
 * @since 2022/10/11 16:40
 */
@SpringBootConfiguration
public class EsConfig {


    @Bean
    public ElasticsearchClient esClient(){
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "123456"));
        // Create the low-level client
        RestClient httpClient = RestClient.builder(
                new HttpHost("127.0.0.1", 9200)
        ).setFailureListener(new FailureListener())
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching();
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    httpClientBuilder.setMaxConnTotal(200);
                    httpClientBuilder.setMaxConnPerRoute(100);
                    httpClientBuilder.setKeepAliveStrategy((response, context) -> Duration.ofMinutes(5).toMillis());
                    httpClientBuilder.disableAuthCaching();
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    return  httpClientBuilder;
                })
                .setRequestConfigCallback(requestConfigBuilder -> {
                    requestConfigBuilder.setAuthenticationEnabled(true);
                    requestConfigBuilder.setConnectTimeout(30000);
                    requestConfigBuilder.setConnectionRequestTimeout(10000);
                    requestConfigBuilder.setSocketTimeout(15000);
                    return requestConfigBuilder;
                })
                .build();

        // Create the Java API Client with the same low level client
        ElasticsearchTransport transport = new RestClientTransport(
                httpClient, new JacksonJsonpMapper()
        );

        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        return esClient;
    }


    public static class FailureListener extends RestClient.FailureListener{
        private static Logger log = LoggerFactory.getLogger(FailureListener.class);
        @Override
        public void onFailure(Node node) {
            log.error("node:{} has failure",node.toString());
        }
    }
}
