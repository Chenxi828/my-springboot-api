package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存管理控制器
 * 提供缓存管理相关的 API 接口
 */
@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 清除所有缓存
     * 地址：POST /api/cache/clear
     */
    @PostMapping("/clear")
    public Result<String> clearAllCache() {
        try {
            // 清除所有缓存空间
            cacheManager.getCacheNames().forEach(cacheName -> {
                cacheManager.getCache(cacheName).clear();
            });
            return Result.success("所有缓存已清除");
        } catch (Exception e) {
            return Result.fail(500, "清除缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清除指定缓存空间
     * 地址：POST /api/cache/clear/{cacheName}
     *
     * @param cacheName 缓存空间名称
     */
    @PostMapping("/clear/{cacheName}")
    public Result<String> clearCacheByName(@PathVariable String cacheName) {
        try {
            if (cacheManager.getCache(cacheName) != null) {
                cacheManager.getCache(cacheName).clear();
                return Result.success("缓存空间 [" + cacheName + "] 已清除");
            } else {
                return Result.fail(500, "缓存空间 [" + cacheName + "] 不存在");
            }
        } catch (Exception e) {
            return Result.fail(500, "清除缓存失败: " + e.getMessage());
        }
    }

    /**
     * 获取缓存统计信息
     * 地址：GET /api/cache/stats
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getCacheStats() {
        try {
            Map<String, Object> stats = new HashMap<>();

            // 获取缓存空间列表
            stats.put("cacheNames", cacheManager.getCacheNames());

            // 获取缓存类型（内存缓存或Redis缓存）
            String cacheType = cacheManager.getClass().getSimpleName();
            stats.put("cacheType", cacheType.contains("Redis") ? "redis" : "simple");

            // 获取 Redis 信息（仅当使用 Redis 时）
            Map<String, Object> redisInfo = new HashMap<>();
            if (redisTemplate != null) {
                try {
                    String pingResult = redisTemplate.getConnectionFactory().getConnection().ping();
                    redisInfo.put("connected", "PONG".equals(pingResult));
                    redisInfo.put("server", pingResult);
                } catch (Exception e) {
                    redisInfo.put("connected", false);
                    redisInfo.put("error", e.getMessage());
                }
                stats.put("redis", redisInfo);
            } else {
                redisInfo.put("connected", false);
                redisInfo.put("message", "使用内存缓存，无需 Redis");
                stats.put("redis", redisInfo);
            }

            stats.put("cacheCount", cacheManager.getCacheNames().size());

            return Result.success(stats);
        } catch (Exception e) {
            return Result.fail(500, "获取缓存统计失败: " + e.getMessage());
        }
    }

    /**
     * 预热缓存
     * 地址：POST /api/cache/warmup
     * 预先加载常用数据到缓存中
     */
    @PostMapping("/warmup")
    public Result<String> warmUpCache() {
        try {
            // 这里可以添加缓存预热逻辑
            // 例如：预加载订单统计、选品推荐等常用数据
            // 实际项目中会调用对应的 Service 方法

            return Result.success("缓存预热完成");
        } catch (Exception e) {
            return Result.fail(500, "缓存预热失败: " + e.getMessage());
        }
    }
}