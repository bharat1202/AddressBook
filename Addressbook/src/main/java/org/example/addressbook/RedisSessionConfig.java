package org.example.addressbook;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession  // This enables Spring Session backed by Redis.
public class RedisSessionConfig {
    // Additional configuration (if needed) goes here.
}
