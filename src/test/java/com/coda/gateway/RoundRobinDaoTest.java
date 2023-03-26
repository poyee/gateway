package com.coda.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RoundRobinDaoTest {
    private RoundRobinDao dao;

    private String[] hosts = new String[] {"abc", "def", "ghi"};
    private String path = "/coda/simple";
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        dao = new RoundRobinDao(hosts, path, restTemplate);
    }

    @Test
    void testRoundRobin() {
        int count = 4;
        for (int i = 0; i < count; i++) {
            dao.request(new HashMap<>());
        }

        verify(restTemplate, times(2)).postForObject(eq("http://abc/coda/simple"), any(), any());
        verify(restTemplate, times(1)).postForObject(eq("http://def/coda/simple"), any(), any());
        verify(restTemplate, times(1)).postForObject(eq("http://ghi/coda/simple"), any(), any());
    }
}
