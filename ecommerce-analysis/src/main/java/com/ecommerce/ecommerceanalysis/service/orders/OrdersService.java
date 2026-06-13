package com.ecommerce.ecommerceanalysis.service.orders;

import com.ecommerce.ecommerceanalysis.entity.Orders;
import java.util.List;
import java.util.Map;

public interface OrdersService {
    List<Orders> listAll();
    List<Orders> listAll(int page, int size);
    List<Map<String, Object>> getCategoryCount();
    List<Map<String, Object>> getCityCount();
    List<Map<String, Object>> getMonthlyTrend();
    Long getTotalOrders();
    Double getTotalAmount();
    List<Map<String, Object>> getDateCount();

    // 带筛选的分页查询
    List<Map<String, Object>> searchOrders(String keyword, String category, int page, int size);

    // 带筛选的总数统计
    Long countSearchOrders(String keyword, String category);

    // 金额统计（带筛选）
    Map<String, Object> getAmountStats(String keyword, String category);

    // 巴西订单统计（支持筛选）
    Map<String, Object> getBrazilOrderStats(String keyword);
}