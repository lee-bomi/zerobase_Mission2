package com.example.Account.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Configuration
public class LocalRedisConfig {
    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
//        redisServer = new RedisServer(redisPort);
        redisServer = RedisServer.builder()
                        .port(redisPort)
                        .setting("maxmemory 128M")
                        .build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
}