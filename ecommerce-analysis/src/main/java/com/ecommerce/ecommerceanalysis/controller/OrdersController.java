package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.entity.Result;
import com.ecommerce.ecommerceanalysis.entity.Orders;
import com.ecommerce.ecommerceanalysis.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping("/listAll")
    public Result<List<Orders>> listAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(ordersService.listAll(page, size));
    }

    @GetMapping("/category-count")
    public Result<List<Map<String, Object>>> getCategoryCount() {
        return Result.success(ordersService.getCategoryCount());
    }

    @GetMapping("/city-count")
    public Result<List<Map<String, Object>>> getCityCount() {
        return Result.success(ordersService.getCityCount());
    }

    @GetMapping("/monthly-trend")
    public Result<List<Map<String, Object>>> getMonthlyTrend() {
        return Result.success(ordersService.getMonthlyTrend());
    }

    /**
     * 巴西订单统计（支持筛选）
     * GET /api/orders/stats?keyword=xxx
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getBrazilStats(
            @RequestParam(required = false) String keyword) {
        String k = keyword == null ? "" : keyword;
        return Result.success(ordersService.getBrazilOrderStats(k));
    }

    /**
     * 订单列表（带分页、关键词、品类筛选）
     * GET /api/orders/list?page=1&size=10&keyword=&category=
     */
    @GetMapping("/list")
    public Result<List<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        return Result.success(ordersService.searchOrders(keyword, category, page, size));
    }

    /**
     * 订单总数（带筛选）
     * GET /api/orders/total?keyword=&category=
     */
    @GetMapping("/total")
    public Result<Map<String, Object>> total(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("total", ordersService.countSearchOrders(keyword, category));
        return Result.success(map);
    }

    /**
     * 订单金额统计（带筛选）
     * GET /api/orders/amount?keyword=&category=
     */
    @GetMapping("/amount")
    public Result<Map<String, Object>> amount(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category) {
        return Result.success(ordersService.getAmountStats(keyword, category));
    }

    /**
     * 日期分布
     * GET /api/orders/date-count
     */
    @GetMapping("/date-count")
    public Result<List<Map<String, Object>>> dateCount() {
        return Result.success(ordersService.getDateCount());
    }
}