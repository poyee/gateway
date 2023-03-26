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


    @Value("${hosts}")
    private String[] hosts;

    @Value("${simple.path}")
    private String path;

    @Autowired
    private RestTemplate restTemplate;

    private AtomicInteger counter = new AtomicInteger();

    public Map<String, Object> request(Map<String, Object> body) {
        String host = hosts[counter.get()];
        counter.set((counter.get() + 1) % hosts.length);

        String url = "http://" + host + path;

        LOGGER.info("request " + url);

        return restTemplate.postForObject(url, body, Map.class);
    }
}
