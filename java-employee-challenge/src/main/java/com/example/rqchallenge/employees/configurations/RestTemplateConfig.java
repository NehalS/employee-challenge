package com.example.rqchallenge.employees.configurations;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/** This class deals with configurations required for RestTemplate
 */

@Configuration
public class RestTemplateConfig {

    @Value("${config.connection.maxConnections}")
    private Integer maxConnections;

    @Value("${config.connection.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${config.connection.connectTimeout}")
    private Integer connectionTimeOut;

    @Value("${config.connection.socketTimeout}")
    private Integer socketTimeout;


    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        /** The PoolingHttpClientConnectionManager will create and manage a pool of connections for each route or target host we use.
         * The default size of the pool of concurrent connections that can be open by the manager is 2 for each route or target host,
         * and 20 for total open connections.
         */
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(maxConnections);
        return manager;
    }

    @Bean
    public RequestConfig requestConfig() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout) // timeout from sending request to receive response
                .setConnectTimeout(connectionTimeOut) // timeout for establishing connection to server
                .setSocketTimeout(socketTimeout) // timeout for receiving individual packets over internet.
                .build();
        return requestConfig;
    }

    @Bean
    public CloseableHttpClient httpClient(
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        CloseableHttpClient result = HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        return result;
    }

    @Bean
    public RestTemplate restTemplate(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(requestFactory);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}