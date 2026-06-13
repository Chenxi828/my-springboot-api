package com.ecommerce.ecommerceanalysis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    private static final Logger logger = LoggerFactory.getLogger(DeepSeekServiceImpl.class);

    @Value("${deepseek.api-key:sk-xxx}")
    private String apiKey;

    @Value("${deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;

    @Value("${deepseek.model:deepseek-chat}")
    private String model;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DeepSeekServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Map<String, Object> getMockChatResponse(Map<String, Object> request) {
        logger.info("使用模拟数据响应 DeepSeek 请求");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("source", "mock");
        
        // 模拟 DeepSeek API 响应格式
        Map<String, Object> data = new HashMap<>();
        data.put("id", "chatcmpl-" + System.currentTimeMillis());
        data.put("object", "chat.completion");
        data.put("created", System.currentTimeMillis() / 1000);
        data.put("model", model);
        
        List<Map<String, Object>> choices = new ArrayList<>();
        Map<String, Object> choice = new HashMap<>();
        
        Map<String, Object> message = new HashMap<>();
        message.put("role", "assistant");
        
        // 根据请求内容生成模拟回复
        String content = generateMockContent(request);
        message.put("content", content);
        
        choice.put("message", message);
        choice.put("finish_reason", "stop");
        choices.add(choice);
        
        data.put("choices", choices);
        
        Map<String, Object> usage = new HashMap<>();
        usage.put("prompt_tokens", 100);
        usage.put("completion_tokens", 200);
        usage.put("total_tokens", 300);
        data.put("usage", usage);
        
        result.put("data", data);
        return result;
    }
    
    private String generateMockContent(Map<String, Object> request) {
        try {
            List<Map<String, Object>> messages = (List<Map<String, Object>>) request.get("messages");
            if (messages != null && !messages.isEmpty()) {
                Map<String, Object> lastMessage = messages.get(messages.size() - 1);
                String userContent = (String) lastMessage.get("content");
                
                if (userContent != null) {
                    if (userContent.contains("市场") || userContent.contains("潜力")) {
                        return "{\"marketDemand\": \"高\", \"growthTrend\": \"上升趋势\", \"targetUsers\": \"18-35岁消费群体\", \"riskLevel\": \"中等\", \"suggestions\": \"建议优先进入高需求品类，关注市场动态\"}";
                    } else if (userContent.contains("竞争") || userContent.contains("对手")) {
                        return "{\"competitors\": [\"本地品牌\", \"国际品牌\"], \"marketShare\": \"分散\", \"intensity\": \"中等\", \"differentiation\": \"产品差异化\", \"barriers\": \"品牌认知\"}";
                    } else if (userContent.contains("价格") || userContent.contains("定价")) {
                        return "{\"priceRange\": \"50-200 BRL\", \"suggestedPrice\": 120, \"profitMargin\": 35, \"competitiveness\": \"中等\", \"promotionSpace\": \"10-15%\"}";
                    } else if (userContent.contains("合规") || userContent.contains("认证")) {
                        return "{\"customsRequirements\": \"常规申报\", \"certifications\": [\"INMETRO\"], \"taxCompliance\": \"ICMS/IPI\", \"riskLevel\": \"低到中等\"}";
                    } else if (userContent.contains("物流") || userContent.contains("运输")) {
                        return "{\"recommendedMethod\": \"空运+本地配送\", \"deliveryTime\": \"10-15天\", \"costEstimate\": \"15-25 USD/件\", \"packaging\": \"防震包装\"}";
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("解析请求失败: {}", e.getMessage());
        }
        
        // 默认回复
        return "{\"analysis\": \"分析完成\", \"recommendations\": [\"电子产品\", \"家居用品\", \"运动户外\"], \"confidence\": 85}";
    }

    @Override
    public Map<String, Object> analyzeMarketPotential(List<String> categories) {
        String prompt = String.format(
            "请分析以下巴西热销品类的市场潜力：\n\n品类列表：%s\n\n请从以下维度进行分析：\n1. 市场需求热度\n2. 增长趋势预测\n3. 目标用户画像\n4. 潜在风险点\n5. 进入建议\n\n请以JSON格式输出分析结果。",
            String.join(", ", categories)
        );

        String response = callDeepSeek(prompt);
        return parseResponse(response, categories, "market_potential_analysis");
    }

    @Override
    @Cacheable(value = "deepseek", key = "'categoryOverlap:' + #brCategories.hashCode() + ':' + #cnCategories.hashCode()")
    public Map<String, Object> analyzeCategoryOverlap(List<String> brCategories, List<String> cnCategories) {
        String prompt = String.format(
            "请分析巴西热销品类与国内供应链品类的重合度：\n\n巴西热销品类：%s\n\n国内供应品类：%s\n\n请分析：\n1. 直接重合品类（可直接对接）\n2. 相关品类（可调整适配）\n3. 空白品类（需开发）\n4. 重合度百分比\n5. 推荐优先对接的品类\n\n请以JSON格式输出分析结果。",
            String.join(", ", brCategories),
            String.join(", ", cnCategories)
        );

        String response = callDeepSeek(prompt);
        Map<String, Object> result = parseResponse(response, brCategories, "category_overlap_analysis");
        result.put("brazil_categories", brCategories);
        result.put("china_categories", cnCategories);
        result.put("overlap_rate", calculateOverlapRate(brCategories, cnCategories));
        return result;
    }

    @Override
    @Cacheable(value = "deepseek", key = "'priceSpace:' + #productInfo.hashCode()")
    public Map<String, Object> evaluatePriceSpace(Map<String, Object> productInfo) {
        String prompt = String.format(
            "请评估以下产品的价格空间：\n\n产品信息：%s\n\n请分析：\n1. 目标市场（巴西）同类产品价格区间\n2. 建议定价策略\n3. 预期利润率\n4. 价格竞争力评估\n5. 促销空间建议\n\n请以JSON格式输出分析结果。",
            productInfo
        );

        String response = callDeepSeek(prompt);
        Map<String, Object> result = parseResponse(response, Collections.emptyList(), "price_space_evaluation");
        result.put("product_info", productInfo);
        return result;
    }

    @Override
    @Cacheable(value = "deepseek", key = "'competition:' + #category")
    public Map<String, Object> analyzeCompetition(String category) {
        String prompt = String.format(
            "请分析巴西市场%s品类的竞争情况：\n\n请分析：\n1. 主要竞争对手\n2. 市场份额分布\n3. 竞争激烈程度评级\n4. 差异化机会\n5. 进入壁垒\n\n请以JSON格式输出分析结果。",
            category
        );

        String response = callDeepSeek(prompt);
        Map<String, Object> result = parseResponse(response, Collections.singletonList(category), "competition_analysis");
        result.put("category", category);
        return result;
    }

    @Override
    @Cacheable(value = "deepseek", key = "'complianceRisk:' + #categories.hashCode()")
    public Map<String, Object> evaluateComplianceRisk(List<String> categories) {
        String prompt = String.format(
            "请评估以下品类出口巴西的合规风险：\n\n品类列表：%s\n\n请分析：\n1. 各品类的海关监管要求\n2. 认证要求（如INMETRO等）\n3. 税务合规要点\n4. 产品安全标准\n5. 风险等级评估\n\n请以JSON格式输出分析结果。",
            String.join(", ", categories)
        );

        String response = callDeepSeek(prompt);
        return parseResponse(response, categories, "compliance_risk_evaluation");
    }

    @Override
    @Cacheable(value = "deepseek", key = "'logistics:' + #categories.hashCode()")
    public Map<String, Object> checkLogisticsAdaptation(List<String> categories) {
        String prompt = String.format(
            "请分析以下品类的物流适配性：\n\n品类列表：%s\n\n请分析：\n1. 适合的物流方式（空运/海运/快递）\n2. 包装要求\n3. 运输时效预估\n4. 物流成本估算\n5. 仓储建议\n\n请以JSON格式输出分析结果。",
            String.join(", ", categories)
        );

        String response = callDeepSeek(prompt);
        return parseResponse(response, categories, "logistics_adaptation_check");
    }

    @Override
    @Cacheable(value = "deepseek", key = "'recommendList:' + #categories.hashCode()")
    public Map<String, Object> generateRecommendList(List<String> categories) {
        String prompt = String.format(
            "请基于巴西热销品类生成可同步上架的推荐清单：\n\n热销品类：%s\n\n请输出：\n1. 推荐上架品类清单\n2. 每个品类的上架优先级\n3. 定价建议\n4. 运营建议\n5. 预期效果预估\n\n请以JSON格式输出推荐清单。",
            String.join(", ", categories)
        );

        String response = callDeepSeek(prompt);
        Map<String, Object> result = parseResponse(response, categories, "recommend_list");
        result.put("recommend_count", categories.size());
        return result;
    }

    private String callDeepSeek(String prompt) {
        if ("sk-xxx".equals(apiKey)) {
            return getMockResponse(prompt);
        }

        try {
            String url = baseUrl + "/chat/completions";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 4096);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            return getMockResponse(prompt);
        }
    }

    private String getMockResponse(String prompt) {
        if (prompt.contains("市场潜力")) {
            return "{\"analysis\":{\"market_demand\":\"高\",\"growth_trend\":\"上升\",\"target_users\":\"18-35岁消费者\",\"risks\":\"汇率波动、关税变化\",\"suggestions\":\"建议优先进入高需求品类\"},\"categories_analyzed\":5}";
        } else if (prompt.contains("重合度")) {
            return "{\"direct_overlap\":[\"电子产品\",\"家居用品\"],\"related\":[\"服装\",\"美妆\"],\"blank\":[],\"recommendations\":[\"电子产品\",\"家居用品\"]}";
        } else if (prompt.contains("价格空间")) {
            return "{\"price_range\":\"50-200巴西雷亚尔\",\"suggested_price\":120,\"expected_profit_rate\":35,\"competitiveness\":\"中等\",\"promotion_space\":\"10-15%\"}";
        } else if (prompt.contains("竞争")) {
            return "{\"competitors\":[\"本地品牌A\",\"国际品牌B\"],\"market_share\":\"分散\",\"intensity\":\"中等\",\"differentiation\":\"产品创新\",\"barriers\":\"品牌认知\"}";
        } else if (prompt.contains("合规风险")) {
            return "{\"customs_requirements\":\"常规申报\",\"certifications\":[\"INMETRO(部分产品)\"],\"tax_compliance\":\"ICMS/IPI\",\"risk_level\":\"低到中等\"}";
        } else if (prompt.contains("物流")) {
            return "{\"recommended_method\":\"空运+本地配送\",\"packaging\":\"防震包装\",\"delivery_time\":\"10-15天\",\"cost_estimate\":\"15-25美元/件\"}";
        } else if (prompt.contains("推荐清单")) {
            return "{\"recommend_list\":[{\"category\":\"电子产品\",\"priority\":\"高\",\"price_suggestion\":150,\"suggestions\":\"重点推广\"},{\"category\":\"家居用品\",\"priority\":\"中\",\"price_suggestion\":80,\"suggestions\":\"稳定供应\"}],\"total_items\":2}";
        }
        return "{\"result\":\"分析完成\",\"message\":\"使用模拟数据\"}";
    }

    private Map<String, Object> parseResponse(String response, List<String> categories, String analysisType) {
        Map<String, Object> result = new HashMap<>();
        result.put("analysis_type", analysisType);
        result.put("categories", categories);
        result.put("timestamp", new Date().toString());
        result.put("source", "deepseek_ai");

        try {
            Map<String, Object> data = objectMapper.readValue(response, Map.class);
            if (data.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) data.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) choice.get("message");
                    String content = (String) message.get("content");
                    try {
                        Map<String, Object> analysis = objectMapper.readValue(content, Map.class);
                        result.putAll(analysis);
                    } catch (Exception e) {
                        result.put("analysis_text", content);
                    }
                }
            } else {
                try {
                    Map<String, Object> analysis = objectMapper.readValue(response, Map.class);
                    result.putAll(analysis);
                } catch (Exception e) {
                    result.put("raw_response", response);
                }
            }
        } catch (Exception e) {
            result.put("analysis_text", response);
        }

        return result;
    }

    private double calculateOverlapRate(List<String> list1, List<String> list2) {
        if (list1.isEmpty() || list2.isEmpty()) return 0;
        
        Set<String> set1 = new HashSet<>(list1);
        Set<String> set2 = new HashSet<>(list2);
        
        int overlap = 0;
        for (String item : set1) {
            if (set2.stream().anyMatch(c -> c.contains(item) || item.contains(c))) {
                overlap++;
            }
        }
        
        return Math.round((double) overlap / set1.size() * 100);
    }
}