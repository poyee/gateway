package com.coda.gateway;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RoundRobinDao {
    private static Logger LOGGER = LoggerFactory.getLogger(RoundRobinDao.class);

    private final String[] hosts;
    private final String path;
    private final RestTemplate restTemplate;
    private final AtomicInteger counter;
    private final int maxRetry = 3;

    @Autowired
    public RoundRobinDao(@Value("${hosts}") String[] hosts,
                         @Value("${simple.path}") String path,
                         @Autowired RestTemplate restTemplate) {
        this.hosts = hosts;
        this.path = path;
        this.restTemplate = restTemplate;
        this.counter = new AtomicInteger();
    }

    public Map<String, Object> request(Map<String, Object> body) {
        int hostNumber = counter.get();
        counter.set((hostNumber + 1) % hosts.length);

        return request(body, hostNumber, 0);
    }

    public Map<String, Object> request(Map<String, Object> body, int hostNumber, int retry) {
        String host = hosts[hostNumber];
        String url = "http://" + host + path;

        LOGGER.info("request {}", url);

        try {
            return restTemplate.postForObject(url, body, Map.class);
        } catch (Exception e) {
            if (retry < maxRetry) {
                LOGGER.info("{} return error, retrying", url);
                return request(body, (hostNumber + 1) % hosts.length, retry + 1);
            }

            throw e;
        }
    }
}
