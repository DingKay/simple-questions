package com.dk.web.session;

import com.dk.config.InitConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author dkay
 * @version 1.0
 */
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 10 * 60)
public class App {
    public static void main(String[] args) {
        InitConfig.initializationLogger();
        SpringApplication.run(App.class);
    }
}
