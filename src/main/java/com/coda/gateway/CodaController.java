package com.coda.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("coda")
public class CodaController {
    @Autowired
    RoundRobinDao dao;


    @PostMapping("/gateway")
    public Map<String, Object> gateway(@RequestBody Map<String, Object> body) {
        return dao.request(body);
    }

    @PostMapping("/simple")
    public Map<String, Object> getSimple(@RequestBody Map<String, Object> body) {
        return body;
    }
}
