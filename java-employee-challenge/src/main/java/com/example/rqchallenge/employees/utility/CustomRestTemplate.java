package com.example.rqchallenge.employees.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class CustomRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    /** This generic method is used to invoke REST API. Exception thrown has to be handled.
     *  This method internally retries on 502, 504, 429 HTTP status codes
     * @param request actual request object
     * @param respClass object type of response will be returned
     * @param contentType content type of request to be sent.
     * @param httpMethod HTTP method for request
     * @param headersMap header parameters in key-value pair.
     * @param url URL which to be invoked
     * @return returns response mapped to RS type passed in parameter.
     * @param <RQ> type of request object
     * @param <RS> type of resposne obejct
     */
    @Retryable(
            retryFor = {HttpServerErrorException.BadGateway.class,
                        HttpServerErrorException.GatewayTimeout.class,
                        HttpClientErrorException.TooManyRequests.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000))
    public <RQ, RS> RS execute(RQ request, Class<RS> respClass,
                               Optional<MediaType> contentTypeOptional,
                               HttpMethod httpMethod,
                               Map<String, String> headersMap,
                               String url) {
        log.info("Sending request on url {}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
        if(contentTypeOptional.isPresent())
            headers.setContentType(contentTypeOptional.get());

        if(headersMap != null && !headersMap.isEmpty()) {
            for(Map.Entry<String, String> entry : headersMap.entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<RQ> requestEntity = (HttpEntity<RQ>) new HttpEntity<>(request, headers);
        ResponseEntity<RS> responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, respClass);
        RS response = responseEntity.getBody();

        return response;
    }
}
