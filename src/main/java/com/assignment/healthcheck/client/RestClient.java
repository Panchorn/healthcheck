package com.assignment.healthcheck.client;

import static io.vavr.API.Try;

import com.assignment.healthcheck.model.Report;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("jsonHttpHeader")
    private HttpHeaders jsonHttpHeader;

    @Autowired
    @Qualifier("httpHeader")
    private HttpHeaders httpHeader;

    public Try<ResponseEntity> get(String endpoint) {
        return Try(() -> restTemplate.exchange(endpoint, HttpMethod.GET, new HttpEntity<>(httpHeader), String.class));
    }

    public Try<ResponseEntity> post(String endpoint, Report report) {
        HttpEntity<Report> httpEntity = new HttpEntity<>(report, jsonHttpHeader);
        return Try(() -> restTemplate.exchange(endpoint, HttpMethod.POST, httpEntity, String.class));
    }

}
