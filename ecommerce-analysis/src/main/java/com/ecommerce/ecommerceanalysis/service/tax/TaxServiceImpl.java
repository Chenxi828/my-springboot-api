package com.ecommerce.ecommerceanalysis.service.tax;

import com.ecommerce.ecommerceanalysis.mapper.TaxRatesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class TaxServiceImpl implements TaxService {

    private static final Logger logger = LoggerFactory.getLogger(TaxServiceImpl.class);
    
    // 统一使用一个随机数对象，避免每次请求数值都变化
    private final Random random = new Random();

    @Resource
    private TaxRatesMapper taxRatesMapper;

    @Override
    public Map<String, Object> getTaxRates(String country, String category) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 优先从数据库获取
            Map<String, Object> dbTaxRate = taxRatesMapper.getTaxRate(country, category);
            
            if (dbTaxRate != null && !dbTaxRate.isEmpty()) {
                result.putAll(dbTaxRate);
                result.put("countryName", dbTaxRate.get("countryName"));
                result.put("importTaxRate", dbTaxRate.get("importTaxRate"));
                result.put("vatRate", dbTaxRate.get("vatRate"));
                result.put("consumptionTaxRate", dbTaxRate.get("consumptionTaxRate"));
                result.put("effectiveTaxRate", dbTaxRate.get("effectiveTaxRate"));
                result.put("hsCodeSuggestion", dbTaxRate.get("hsCode"));
                result.put("recentPolicyChanges", dbTaxRate.get("policyChanges"));
                result.put("taxPlanningTips", dbTaxRate.get("taxTips"));
            } else {
                // 数据库无数据时返回默认值
                result.put("country", country);
                result.put("countryName", country.equals("brazil") ? "巴西" : "中国");
                result.put("category", category);
                result.put("importTaxRate", country.equals("brazil") ? 10.0 : 0.0);
                result.put("vatRate", country.equals("brazil") ? 17.0 : 13.0);
                result.put("consumptionTaxRate", 0.0);
                result.put("effectiveTaxRate", country.equals("brazil") ? 28.87 : 13.0);
                result.put("hsCodeSuggestion", "请根据商品详情确认");
                result.put("recentPolicyChanges", getPolicyChanges(country));
                result.put("taxPlanningTips", getTaxTips(country, category));
            }
        } catch (Exception e) {
            logger.error("获取税率失败 - country: {}, category: {}", country, category, e);
            // 出错时返回默认值
            result.put("country", country);
            result.put("countryName", country.equals("brazil") ? "巴西" : "中国");
            result.put("category", category);
            result.put("importTaxRate", country.equals("brazil") ? 10.0 : 0.0);
            result.put("vatRate", country.equals("brazil") ? 17.0 : 13.0);
            result.put("consumptionTaxRate", 0.0);
            result.put("effectiveTaxRate", country.equals("brazil") ? 28.87 : 13.0);
            result.put("hsCodeSuggestion", "请根据商品详情确认");
            result.put("recentPolicyChanges", getPolicyChanges(country));
            result.put("taxPlanningTips", "数据加载异常，请稍后重试");
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getAllTaxRates() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try {
            List<Map<String, Object>> dbRates = taxRatesMapper.getAllTaxRates();
            if (dbRates != null && !dbRates.isEmpty()) {
                result = dbRates;
            } else {
                // 数据库为空时返回内置数据
                result = getDefaultTaxRates();
            }
        } catch (Exception e) {
            logger.error("获取所有税率失败", e);
            result = getDefaultTaxRates();
        }
        
        return result;
    }

    @Override
    public Map<String, Object> calculateTax(String country, String category, double costPrice, int quantity, double logisticsCost, double exchangeRate) {
        Map<String, Object> result = new HashMap<>();
        
        // 空值防护
        if (costPrice <= 0) {
            costPrice = 100.0;
            logger.warn("成本价格无效，使用默认值: {}", costPrice);
        }
        if (quantity <= 0) {
            quantity = 1;
            logger.warn("数量无效，使用默认值: {}", quantity);
        }
        if (logisticsCost < 0) {
            logisticsCost = 0;
            logger.warn("物流费用无效，使用默认值: {}", logisticsCost);
        }
        if (exchangeRate <= 0) {
            exchangeRate = getDefaultExchangeRate(country);
            logger.warn("汇率无效，使用默认值: {}", exchangeRate);
        }
        
        try {
            // 获取税率配置
            Map<String, Object> taxRates = getTaxRates(country, category);
            double importTaxRate = toDouble(taxRates.get("importTaxRate"));
            double vatRate = toDouble(taxRates.get("vatRate"));
            double consumptionTaxRate = toDouble(taxRates.get("consumptionTaxRate"));
            
            // 计算各项成本
            double totalCostPrice = costPrice * quantity;
            double cifValue = totalCostPrice + logisticsCost;
            
            // 计算税费
            double importTax = cifValue * (importTaxRate / 100);
            double baseForVat = cifValue + importTax;
            double vat = baseForVat * (vatRate / 100);
            double consumptionTax = cifValue * (consumptionTaxRate / 100);
            double totalTax = importTax + vat + consumptionTax;
            
            // 计算总成本和利润率
            double totalCost = cifValue + totalTax;
            double taxRate = cifValue > 0 ? (totalTax / cifValue) * 100 : 0;
            
            // 转换为目标货币
            double totalCostInBRL = totalCost * exchangeRate;
            double unitCostInBRL = totalCostInBRL / quantity;
            
            // 建议售价（基于成本的1.8倍）
            double suggestedPricePerUnit = unitCostInBRL * 1.8;
            double unitProfit = suggestedPricePerUnit - unitCostInBRL;
            double profitMargin = suggestedPricePerUnit > 0 ? (unitProfit / suggestedPricePerUnit) * 100 : 0;
            
            // 盈亏平衡点
            double breakEvenQuantity = unitProfit > 0 ? Math.ceil(1000 / unitProfit) : 0;
            
            // 平台费用（假设15%）
            double platformFee = suggestedPricePerUnit * 0.15;
            double netProfit = unitProfit - platformFee;
            double netProfitMargin = suggestedPricePerUnit > 0 ? (netProfit / suggestedPricePerUnit) * 100 : 0;
            
            // 构建结果
            result.put("country", country);
            result.put("category", category);
            result.put("costPrice", round2(costPrice));
            result.put("quantity", quantity);
            result.put("logisticsCost", round2(logisticsCost));
            result.put("exchangeRate", exchangeRate);
            
            // 成本明细
            result.put("totalCostPrice", round2(totalCostPrice));
            result.put("cifValue", round2(cifValue));
            
            // 税费明细
            result.put("importTax", round2(importTax));
            result.put("vat", round2(vat));
            result.put("consumptionTax", round2(consumptionTax));
            result.put("totalTax", round2(totalTax));
            result.put("taxRate", round2(taxRate));
            
            // 总成本
            result.put("totalCost", round2(totalCost));
            result.put("totalCostInBRL", round2(totalCostInBRL));
            result.put("unitCostInBRL", round2(unitCostInBRL));
            
            // 利润分析
            result.put("suggestedPrice", "R$ " + round2(suggestedPricePerUnit));
            result.put("unitProfit", round2(unitProfit));
            result.put("profitMargin", round2(profitMargin));
            result.put("platformFee", round2(platformFee));
            result.put("netProfit", round2(netProfit));
            result.put("netProfitMargin", round2(netProfitMargin));
            result.put("breakEvenQuantity", breakEvenQuantity);
            
            // 利润等级
            result.put("profitLevel", profitMargin >= 50 ? "高利润" : profitMargin >= 30 ? "中等利润" : "低利润");
            
            // 风险提示
            result.put("riskWarning", getRiskWarning(country, category));
            
        } catch (Exception e) {
            logger.error("计算税费失败 - country: {}, category: {}", country, category, e);
            result.put("error", "计算失败，请稍后重试");
            result.put("country", country);
            result.put("category", category);
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getExchangeRate(String from, String to) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 优先从数据库获取
            Map<String, Object> dbRate = taxRatesMapper.getExchangeRate(from, to);
            
            if (dbRate != null && !dbRate.isEmpty()) {
                result.put("from", dbRate.get("fromCurrency"));
                result.put("to", dbRate.get("toCurrency"));
                result.put("rate", dbRate.get("rate"));
                result.put("timestamp", dbRate.get("updateTime"));
                result.put("source", "数据库汇率");
            } else {
                // 使用默认汇率
                double defaultRate = getDefaultExchangeRateByPair(from, to);
                result.put("from", from);
                result.put("to", to);
                result.put("rate", defaultRate);
                result.put("timestamp", new Date().toString());
                result.put("source", "参考汇率（默认）");
            }
        } catch (Exception e) {
            logger.error("获取汇率失败 - from: {}, to: {}", from, to, e);
            result.put("from", from);
            result.put("to", to);
            result.put("rate", getDefaultExchangeRateByPair(from, to));
            result.put("timestamp", new Date().toString());
            result.put("source", "参考汇率（默认）");
        }
        
        return result;
    }

    // ==================== 私有方法 ====================

    /**
     * 获取默认汇率（根据国家）
     */
    private double getDefaultExchangeRate(String country) {
        if ("brazil".equalsIgnoreCase(country)) {
            return 5.5; // CNY to BRL
        }
        return 1.0;
    }

    /**
     * 获取默认汇率（根据货币对）
     */
    private double getDefaultExchangeRateByPair(String from, String to) {
        String pair = from.toUpperCase() + "_" + to.toUpperCase();
        switch (pair) {
            case "CNY_BRL": return 5.5;
            case "BRL_CNY": return 0.18;
            case "CNY_USD": return 0.14;
            case "USD_CNY": return 7.24;
            default: return 1.0;
        }
    }

    /**
     * 获取默认税率数据（内置）
     */
    private List<Map<String, Object>> getDefaultTaxRates() {
        List<Map<String, Object>> result = new ArrayList<>();
        String[] categories = {"服装配饰", "电子产品", "家居用品", "美妆护肤", "母婴用品", 
                               "运动户外", "食品饮料", "宠物用品", "汽车配件", "数码配件"};
        
        for (String country : Arrays.asList("brazil", "china")) {
            for (String category : categories) {
                Map<String, Object> item = new HashMap<>();
                item.put("country", country);
                item.put("countryName", country.equals("brazil") ? "巴西" : "中国");
                item.put("category", category);
                item.put("importTaxRate", country.equals("brazil") ? 10.0 : 0.0);
                item.put("vatRate", country.equals("brazil") ? 17.0 : 13.0);
                item.put("consumptionTaxRate", 0.0);
                item.put("effectiveTaxRate", country.equals("brazil") ? 28.87 : 13.0);
                item.put("hsCode", "待确认");
                result.add(item);
            }
        }
        return result;
    }

    private String getPolicyChanges(String country) {
        if (country.equals("brazil")) {
            return "2024年巴西政府宣布降低部分消费品进口关税，电子产品进口关税从15%降至10%，有利于跨境电商发展。";
        } else {
            return "中国跨境电商综合试验区政策持续优化，支持跨境电商企业发展，出口退税政策稳定。";
        }
    }

    private String getTaxTips(String country, String category) {
        if (country.equals("brazil")) {
            return "建议通过正规清关渠道进口，确保HS编码准确。巴西税务监管严格，合规申报可避免额外处罚。";
        } else {
            return "中国出口企业可申请出口退税，建议合理利用跨境电商综试区政策优惠。";
        }
    }

    private String getRiskWarning(String country, String category) {
        if (country.equals("brazil") && category.equals("美妆护肤")) {
            return "美妆产品进口巴西需提前申请ANVISA认证，认证周期约3-6个月，请提前规划。";
        } else if (country.equals("brazil")) {
            return "巴西清关周期较长，建议预留足够时间，选择可靠的物流合作伙伴。";
        } else {
            return "国内销售需遵守相关法规，食品类需办理SC认证。";
        }
    }

    /**
     * 安全转换为double
     */
    private double toDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * 保留两位小数
     */
    private double round2(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
