package com.ecommerce.ecommerceanalysis.service.tax;

import java.util.List;
import java.util.Map;

public interface TaxService {

    /**
     * 获取指定国家和品类的税率信息
     */
    Map<String, Object> getTaxRates(String country, String category);

    /**
     * 获取所有国家税率列表
     */
    List<Map<String, Object>> getAllTaxRates();

    /**
     * 计算税费和利润
     */
    Map<String, Object> calculateTax(String country, String category, double costPrice, int quantity, double logisticsCost, double exchangeRate);

    /**
     * 获取汇率
     */
    Map<String, Object> getExchangeRate(String from, String to);
}
