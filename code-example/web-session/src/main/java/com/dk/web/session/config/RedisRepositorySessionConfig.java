package com.dk.web.session.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author dkay
 * @version 1.0
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 15 * 60)
public class RedisRepositorySessionConfig {
}
