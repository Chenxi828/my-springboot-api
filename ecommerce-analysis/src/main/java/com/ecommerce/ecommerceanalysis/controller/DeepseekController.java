package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.entity.Result;
import com.ecommerce.ecommerceanalysis.service.DeepSeekService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DeepSeek AI 分析接口控制器
 * 直接调用真实的 DeepSeek API
 */
@RestController
@RequestMapping("/api/deepseek")
public class DeepseekController {

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;

    @Value("${deepseek.model:deepseek-chat}")
    private String model;

    @Value("${deepseek.timeout:60000}")
    private int timeout;

    @Resource
    private DeepSeekService deepSeekService;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DeepseekController() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 前端 deepseekAPI 调用的核心接口
     * 地址：POST /api/deepseek/chat
     * 优先调用真实的 DeepSeek API，密钥无效时返回模拟数据
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, Object> request) {
        // 检查 API 密钥是否有效（非空且不是默认占位符）
        boolean hasValidKey = apiKey != null && !apiKey.trim().isEmpty() && 
                             !apiKey.equals("your-api-key-here") && 
                             !apiKey.equals("sk-xxx");
        
        if (!hasValidKey) {
            // 密钥无效，返回模拟数据
            return ResponseEntity.ok(deepSeekService.getMockChatResponse(request));
        }
        
        // 密钥有效，调用真实的 DeepSeek API
        try {
            String url = baseUrl + "/chat/completions";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<Map<String, Object>> httpRequest = new HttpEntity<>(request, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest, String.class);

            // 解析响应并返回
            Map<String, Object> deepseekResponse = objectMapper.readValue(response.getBody(), Map.class);
            
            Map<String, Object> result = new HashMap<>();
            result.put("data", deepseekResponse);
            result.put("success", true);
            result.put("source", "deepseek"); // 标识数据来源
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            // API 调用失败时，降级返回模拟数据
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", true);
            errorResult.put("message", "DeepSeek API 调用失败，已切换到模拟数据: " + e.getMessage());
            errorResult.put("source", "mock"); // 标识数据来源
            
            // 尝试返回模拟数据作为降级方案
            try {
                return ResponseEntity.ok(deepSeekService.getMockChatResponse(request));
            } catch (Exception mockEx) {
                errorResult.put("success", false);
                errorResult.put("error", mockEx.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
            }
        }
    }

    // ==================== 以下为 deepseekBackendAPI 对应的接口 ====================

    /**
     * 市场潜力分析
     * POST /api/deepseek/market-potential
     */
    @PostMapping("/market-potential")
    public Result<Map<String, Object>> marketPotential(@RequestBody Map<String, Object> data) {
        List<String> categories = (List<String>) data.get("categories");
        return Result.success(deepSeekService.analyzeMarketPotential(categories));
    }

    /**
     * 类目重合度分析
     * POST /api/deepseek/category-overlap
     */
    @PostMapping("/category-overlap")
    public Result<Map<String, Object>> categoryOverlap(@RequestBody Map<String, Object> data) {
        List<String> brCategories = (List<String>) data.get("brCategories");
        List<String> cnCategories = (List<String>) data.get("cnCategories");
        return Result.success(deepSeekService.analyzeCategoryOverlap(brCategories, cnCategories));
    }

    /**
     * 价格空间评估
     * POST /api/deepseek/price-space
     */
    @PostMapping("/price-space")
    public Result<Map<String, Object>> priceSpace(@RequestBody Map<String, Object> data) {
        return Result.success(deepSeekService.evaluatePriceSpace(data));
    }

    /**
     * 竞争度分析
     * GET /api/deepseek/competition
     */
    @GetMapping("/competition")
    public Result<Map<String, Object>> competition(@RequestParam(defaultValue = "电子产品") String category) {
        return Result.success(deepSeekService.analyzeCompetition(category));
    }

    /**
     * 合规风险评估
     * POST /api/deepseek/compliance-risk
     */
    @PostMapping("/compliance-risk")
    public Result<Map<String, Object>> complianceRisk(@RequestBody Map<String, Object> data) {
        List<String> categories = (List<String>) data.get("categories");
        return Result.success(deepSeekService.evaluateComplianceRisk(categories));
    }

    /**
     * 物流适配性检查
     * POST /api/deepseek/logistics
     */
    @PostMapping("/logistics")
    public Result<Map<String, Object>> logistics(@RequestBody Map<String, Object> data) {
        List<String> categories = (List<String>) data.get("categories");
        return Result.success(deepSeekService.checkLogisticsAdaptation(categories));
    }

    /**
     * 生成推荐清单
     * POST /api/deepseek/recommend
     */
    @PostMapping("/recommend")
    public Result<Map<String, Object>> recommend(@RequestBody Map<String, Object> data) {
        List<String> categories = (List<String>) data.get("categories");
        return Result.success(deepSeekService.generateRecommendList(categories));
    }

    /**
     * 综合分析（一键分析）
     * POST /api/deepseek/comprehensive
     */
    @PostMapping("/comprehensive")
    public Result<Map<String, Object>> comprehensive(@RequestBody Map<String, Object> data) {
        List<String> categories = (List<String>) data.get("categories");
        
        Map<String, Object> result = new HashMap<>();
        result.put("marketPotential", deepSeekService.analyzeMarketPotential(categories));
        result.put("competition", deepSeekService.analyzeCompetition(categories.isEmpty() ? "电子产品" : categories.get(0)));
        result.put("complianceRisk", deepSeekService.evaluateComplianceRisk(categories));
        result.put("logistics", deepSeekService.checkLogisticsAdaptation(categories));
        result.put("recommendList", deepSeekService.generateRecommendList(categories));
        result.put("timestamp", System.currentTimeMillis());
        
        return Result.success(result);
    }
}
