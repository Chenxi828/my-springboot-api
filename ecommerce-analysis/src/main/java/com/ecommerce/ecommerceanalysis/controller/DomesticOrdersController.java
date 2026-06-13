package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.entity.Result;
import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticProduct;
import com.ecommerce.ecommerceanalysis.service.domestic.DomesticOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/domestic")
public class DomesticOrdersController {

    @Autowired
    private DomesticOrdersService domesticOrdersService;

    @GetMapping("/list")
    public Result<List<DomesticProduct>> list() {
        return Result.success(domesticOrdersService.listAll());
    }

    @GetMapping("/category-count")
    public Result<List<Map<String, Object>>> categoryCount() {
        return Result.success(domesticOrdersService.getCategoryCount());
    }

    @GetMapping("/city-count")
    public Result<List<Map<String, Object>>> cityCount() {
        return Result.success(domesticOrdersService.getCityCount());
    }

    @GetMapping("/monthly-trend")
    public Result<List<Map<String, Object>>> monthlyTrend() {
        return Result.success(domesticOrdersService.getMonthlyTrend());
    }

    @GetMapping("/total")
    public Result<Long> getTotalOrders() {
        return Result.success(domesticOrdersService.getTotalOrders());
    }

    @GetMapping("/amount")
    public Result<Double> getTotalAmount() {
        return Result.success(domesticOrdersService.getTotalAmount());
    }

    // ========== 前端使用的 /api/domestic/order/* 路径 ==========

    /**
     * 订单列表（带分页筛选）
     * GET /api/domestic/order/list?page=1&size=10&keyword=&category=
     */
    @GetMapping("/order/list")
    public Result<List<Map<String, Object>>> orderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        return Result.success(domesticOrdersService.searchOrders(keyword, category, page, size));
    }

    /**
     * 订单总数（带筛选）
     * GET /api/domestic/order/total?keyword=&category=
     */
    @GetMapping("/order/total")
    public Result<Map<String, Object>> orderTotal(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("total", domesticOrdersService.countSearchOrders(keyword, category));
        return Result.success(map);
    }

    /**
     * 金额统计（带筛选）
     * GET /api/domestic/order/amount?keyword=&category=
     */
    @GetMapping("/order/amount")
    public Result<Map<String, Object>> orderAmount(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        // 参数为空时，默认传空字符串
        String k = keyword == null ? "" : keyword;
        String c = category == null ? "" : category;
        return Result.success(domesticOrdersService.getAmountStats(k, c));
    }

    /**
     * 日期分布
     * GET /api/domestic/order/date-count
     */
    @GetMapping("/order/date-count")
    public Result<List<Map<String, Object>>> orderDateCount() {
        return Result.success(domesticOrdersService.getDateCount());
    }

    /**
     * 分类统计（前端 Dashboard 使用）
     * GET /api/domestic/order/count-by-category
     */
    @GetMapping("/order/count-by-category")
    public Result<List<Map<String, Object>>> orderCountByCategory() {
        return Result.success(domesticOrdersService.countByCategoryForMap());
    }
}