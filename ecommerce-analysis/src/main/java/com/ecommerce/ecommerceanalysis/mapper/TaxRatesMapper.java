package com.ecommerce.ecommerceanalysis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface TaxRatesMapper {
    
    /**
     * 获取指定国家和品类的税率
     */
    Map<String, Object> getTaxRate(@Param("country") String country, @Param("category") String category);
    
    /**
     * 获取所有税率列表
     */
    List<Map<String, Object>> getAllTaxRates();
    
    /**
     * 获取指定国家的税率列表
     */
    List<Map<String, Object>> getTaxRatesByCountry(@Param("country") String country);
    
    /**
     * 获取汇率
     */
    Map<String, Object> getExchangeRate(@Param("fromCurrency") String fromCurrency, @Param("toCurrency") String toCurrency);
    
    /**
     * 获取所有汇率
     */
    List<Map<String, Object>> getAllExchangeRates();
}
