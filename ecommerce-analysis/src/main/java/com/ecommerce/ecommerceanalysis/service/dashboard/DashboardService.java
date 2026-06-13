package com.ecommerce.ecommerceanalysis.service.dashboard;

import java.util.Map;

public interface DashboardService {
    
    /**
     * 概览数据：巴西/国内总订单数、总金额等
     */
    Map<String, Object> getOverview();
    
    /**
     * 城市分布统计
     */
    Map<String, Object> getCityStats();
    
    /**
     * 订单状态统计
     */
    Map<String, Object> getStatusStats();
}