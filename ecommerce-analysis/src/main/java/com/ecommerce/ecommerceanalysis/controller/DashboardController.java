package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.entity.Result;
import com.ecommerce.ecommerceanalysis.service.dashboard.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 数据大屏 · 概览统计接口（MyBatis实现）
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * 概览数据：巴西/国内总订单数、总金额等
     * GET /api/dashboard/overview
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        return Result.success(dashboardService.getOverview());
    }

    /**
     * 城市分布统计
     * GET /api/dashboard/city
     */
    @GetMapping("/city")
    public Result<Map<String, Object>> cityStats() {
        return Result.success(dashboardService.getCityStats());
    }

    /**
     * 订单状态统计
     * GET /api/dashboard/status
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> statusStats() {
        return Result.success(dashboardService.getStatusStats());
    }
}
