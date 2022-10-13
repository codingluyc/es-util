package com.luyc.esclient.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

/**
 * @author luyc
 * @since 2022/10/11 16:40
 */
@SpringBootConfiguration
@ConfigurationProperties("elasticsearch")
public class EsConfig {

    private String host = "127.0.0.1";
    private Integer port = 9201;
    private String userName;
    private String password;


    @Bean
    public ElasticsearchClient esClient(){
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
        // Create the low-level client
        RestClient httpClient = RestClient.builder(
                new HttpHost(host, port)
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
                httpClient,
                new JacksonJsonpMapper(new ObjectMapper().registerModule(new JavaTimeModule()))//使用JackSon json解析器，并使用javaTime模块用于适配LocalDateTIme
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


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
