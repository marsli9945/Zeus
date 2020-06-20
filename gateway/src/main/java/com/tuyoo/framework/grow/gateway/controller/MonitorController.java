package com.tuyoo.framework.grow.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
public class MonitorController
{
    @Value("${server.port}")
    private String port;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("monitor")
    public Mono<Object> monitor()
    {
        Map<String, Object> resMap = new HashMap<>();

        // 查看健康状态,决定返回的主体信息
        try
        {
            restTemplate.exchange("http://127.0.0.1:" + port + "/actuator/health", HttpMethod.GET, new HttpEntity<>(new LinkedMultiValueMap<>()), JSONObject.class);
            resMap.put("code", 0);
            resMap.put("message", "ok");
        } catch (Exception e)
        {
            resMap.put("code", 1);
            resMap.put("message", "error");
        }

        // 获取要收集的系统信息
        resMap.put("data", getMetrics());

        return Mono.just(resMap);
    }

    /**
     * 收集系统信息
     *
     * @return 信息集合
     */
    private HashMap<String, Object> getMetrics()
    {
        HashMap<String, Object> metricsMap = new HashMap<>();

        // 先获取要收集的信息名称
        String metricsUrl = "http://127.0.0.1:" + port + "/actuator/metrics";
        ResponseEntity<JSONObject> exchange = restTemplate.exchange(metricsUrl, HttpMethod.GET, new HttpEntity<>(new LinkedMultiValueMap<>()), JSONObject.class);
        List<String> names = JSON.parseArray(Objects.requireNonNull(exchange.getBody()).getJSONArray("names").toString(), String.class);

        // 分别获取信息内容放入集合
        ResponseEntity<JSONObject> exchangeDetail;
        for (String name : names)
        {
            exchangeDetail = restTemplate.exchange(metricsUrl + "/" + name, HttpMethod.GET, new HttpEntity<>(new LinkedMultiValueMap<>()), JSONObject.class);
            metricsMap.put(name, Objects.requireNonNull(exchangeDetail.getBody()).getJSONArray("measurements").getJSONObject(0).getFloat("value"));
        }

        return metricsMap;
    }

}
