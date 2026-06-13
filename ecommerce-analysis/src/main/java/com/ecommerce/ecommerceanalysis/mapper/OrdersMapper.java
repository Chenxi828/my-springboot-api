package com.ecommerce.ecommerceanalysis.mapper;



import com.ecommerce.ecommerceanalysis.entity.Orders;
import com.ecommerce.ecommerceanalysis.entity.domestic.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {
    List<Orders> listAll();
    List<Orders> listAllWithPage(int offset, int size);
    List<DomesticCategoryCount> countByCategory();
    List<DomesticCityCount> countByCity();
    List<DomesticMonthTrend> getMonthlyTrend();
    Long countTotal();
    Double sumAmount();
    List<DomesticDateCount> getDateCount();

    // === 前端分页筛选查询（带关键词和品类过滤）===
    List<Map<String, Object>> searchOrders(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("offset") int offset,
            @Param("size") int size);

    Long countSearchOrders(
            @Param("keyword") String keyword,
            @Param("category") String category);

    // 金额+订单数统计（带筛选）
    Map<String, Object> getAmountStats(
            @Param("keyword") String keyword,
            @Param("category") String category);

    // 巴西订单统计（支持筛选）
    Long getTotalOrderCount(@Param("keyword") String keyword);
    Long getCompletedOrderCount(@Param("keyword") String keyword);
    java.math.BigDecimal getTotalOrderAmount(@Param("keyword") String keyword);
}

