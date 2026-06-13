package com.ecommerce.ecommerceanalysis.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 简单缓存配置类（不依赖Redis）
 * 当 spring.cache.type=simple 时使用此配置
 */
@Configuration
@EnableCaching
public class SimpleCacheConfig {

    @Bean
    @Primary
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        // 预定义缓存名称
        cacheManager.setCacheNames(java.util.Arrays.asList(
                "orders",
                "statistics",
                "selection",
                "supply",
                "dashboard",
                "deepseek"
        ));
        return cacheManager;
    }
}
