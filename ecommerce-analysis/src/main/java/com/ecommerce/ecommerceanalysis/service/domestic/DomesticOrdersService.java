package com.ecommerce.ecommerceanalysis.service.domestic;

import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticProduct;
import java.util.List;
import java.util.Map;

public interface DomesticOrdersService {
    List<DomesticProduct> listAll();
    List<Map<String, Object>> getCategoryCount();
    List<Map<String, Object>> getCityCount();
    List<Map<String, Object>> getMonthlyTrend();
    List<Map<String, Object>>countAllByCategory();
    Long getTotalOrders();
    Double getTotalAmount();
    List<Map<String, Object>> getOrderList();
    List<Map<String, Object>> getDateCount();

    // 带筛选的分页查询
    List<Map<String, Object>> searchOrders(String keyword, String category, int page, int size);

    // 带筛选的总数
    Long countSearchOrders(String keyword, String category);

    // 金额+订单数统计（带筛选）【保留这个】
    Map<String, Object> getAmountStats(String keyword, String category);

    // 分类统计(for /count-by-category)
    List<Map<String, Object>> countByCategoryForMap();

    // 新增：无参默认实现，兼容 Controller 无参调用
    default Map<String, Object> getAmountStats() {
        // 不传筛选条件，默认查全量数据
        return getAmountStats("", "");
    }
}