package com.luyc.esclient.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
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

    //最大连接数
    private Integer maxTotal=200;

    //每个路由的最大连接数
    private Integer maxPerRoute=150;




    @Bean
    public ElasticsearchClient esClient(){
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
        // Create the low-level client
        RestClient httpClient = RestClient.builder(new HttpHost(host, port))
                .setFailureListener(new FailureListener())
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching();
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    httpClientBuilder.setMaxConnTotal(this.maxTotal);
                    httpClientBuilder.setMaxConnPerRoute(this.maxPerRoute);
                    httpClientBuilder.setKeepAliveStrategy((response, context) -> Duration.ofMinutes(5).toMillis());
                    httpClientBuilder.disableAuthCaching();
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    try {
                        httpClientBuilder.setConnectionManager(connectionManager(this.maxTotal,this.maxPerRoute,this.host,this.port,this.maxPerRoute));
                    } catch (IOReactorException e) {
                        throw new RuntimeException(e);
                    }

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



    public PoolingNHttpClientConnectionManager connectionManager(int maxTotal,
                                                                int maxPerRoute, String hostname, int port, int maxRoute) throws IOReactorException {
        ConnectingIOReactor connectingIOReactor = new DefaultConnectingIOReactor();
        PoolingNHttpClientConnectionManager manager = new PoolingNHttpClientConnectionManager(connectingIOReactor);
        manager.setMaxTotal(maxTotal);
        manager.setDefaultMaxPerRoute(maxPerRoute);
        manager.setDefaultConnectionConfig(ConnectionConfig.DEFAULT);
//        HttpHost httpHost = new HttpHost(hostname, port);
//        manager.setMaxPerRoute(new HttpRoute(httpHost),maxRoute);
        return manager;
//        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
//                .getSocketFactory();
//        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
//                .getSocketFactory();
//        Registry<ConnectionSocketFactory> registry = RegistryBuilder
//                .<ConnectionSocketFactory> create().register("http", plainsf)
//                .register("https", sslsf).build();
//        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
//                registry);
//        // 将最大连接数增加
//        cm.setMaxTotal(maxTotal);
//        // 将每个路由基础的连接增加
//        cm.setDefaultMaxPerRoute(maxPerRoute);
//        HttpHost httpHost = new HttpHost(hostname, port);
//        // 将目标主机的最大连接数增加
//        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);
//        return cm;
    }

    public CloseableHttpClient createHttpClient(int maxTotal,
                                                int maxPerRoute, int maxRoute, String hostname, int port) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();

        return httpClient;
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
