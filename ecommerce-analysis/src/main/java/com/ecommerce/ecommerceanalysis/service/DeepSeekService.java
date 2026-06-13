package com.ecommerce.ecommerceanalysis.service;

import java.util.List;
import java.util.Map;

public interface DeepSeekService {

    /**
     * 获取模拟聊天响应（当 API 密钥无效时使用）
     */
    Map<String, Object> getMockChatResponse(Map<String, Object> request);

    Map<String, Object> analyzeMarketPotential(List<String> categories);

    Map<String, Object> analyzeCategoryOverlap(List<String> brCategories, List<String> cnCategories);

    Map<String, Object> evaluatePriceSpace(Map<String, Object> productInfo);

    Map<String, Object> analyzeCompetition(String category);

    Map<String, Object> evaluateComplianceRisk(List<String> categories);

    Map<String, Object> checkLogisticsAdaptation(List<String> categories);

    Map<String, Object> generateRecommendList(List<String> categories);
}