package com.ecommerce.ecommerceanalysis.mapper;

import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticOrders;
import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticCategoryCount;
import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticCityCount;
import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticMonthTrend;
import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticDateCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface DomesticOrdersMapper {

    List<DomesticOrders> listAll();

    List<DomesticCategoryCount> countByCategory();

    List<DomesticCityCount> countByCity();

    List<DomesticMonthTrend> getMonthlyTrend();

    Long countTotal();

    Double sumAmount();

    List<DomesticDateCount> getDateCount();

    // === 分页筛选查询 ===
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

    // 分类统计（返回Map给前端）
    List<Map<String, Object>> countByCategoryForMap();

    // 供应链列表（从 domestic_products 表获取）
    List<Map<String, Object>> getSupplyList();
}