package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.entity.Result;
import com.ecommerce.ecommerceanalysis.service.tax.TaxService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

    @Resource
    private TaxService taxService;

    /**
     * 获取税率数据
     * GET /api/tax/rates?country=brazil&category=服装配饰
     */
    @GetMapping("/rates")
    public Result<Map<String, Object>> getTaxRates(
            @RequestParam(defaultValue = "brazil") String country,
            @RequestParam(defaultValue = "服装配饰") String category) {
        return Result.success(taxService.getTaxRates(country, category));
    }

    /**
     * 获取所有国家税率列表
     * GET /api/tax/rates/list
     */
    @GetMapping("/rates/list")
    public Result<List<Map<String, Object>>> getAllTaxRates() {
        return Result.success(taxService.getAllTaxRates());
    }

    /**
     * 计算税费和利润
     * POST /api/tax/calculate
     * {
     *   "country": "brazil",
     *   "category": "服装配饰",
     *   "costPrice": 100,
     *   "quantity": 100,
     *   "logisticsCost": 15
     * }
     */
    @PostMapping("/calculate")
    public Result<Map<String, Object>> calculateTax(@RequestBody Map<String, Object> params) {
        String country = (String) params.getOrDefault("country", "brazil");
        String category = (String) params.getOrDefault("category", "服装配饰");
        double costPrice = params.get("costPrice") != null ? Double.parseDouble(params.get("costPrice").toString()) : 0;
        int quantity = params.get("quantity") != null ? Integer.parseInt(params.get("quantity").toString()) : 1;
        double logisticsCost = params.get("logisticsCost") != null ? Double.parseDouble(params.get("logisticsCost").toString()) : 0;
        double exchangeRate = params.get("exchangeRate") != null ? Double.parseDouble(params.get("exchangeRate").toString()) : 5.5;
        
        return Result.success(taxService.calculateTax(country, category, costPrice, quantity, logisticsCost, exchangeRate));
    }

    /**
     * 获取汇率
     * GET /api/tax/exchange-rate?from=CNY&to=BRL
     */
    @GetMapping("/exchange-rate")
    public Result<Map<String, Object>> getExchangeRate(
            @RequestParam(defaultValue = "CNY") String from,
            @RequestParam(defaultValue = "BRL") String to) {
        return Result.success(taxService.getExchangeRate(from, to));
    }
}
