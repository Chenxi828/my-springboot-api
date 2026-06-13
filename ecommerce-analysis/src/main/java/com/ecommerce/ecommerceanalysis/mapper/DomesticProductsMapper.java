package com.ecommerce.ecommerceanalysis.mapper;

import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticProduct;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface DomesticProductsMapper {

    // 查所有（给 list 接口用）
    List<DomesticProduct> listAll();

    // 按分类统计（category-count）
    List<Map<String, Object>> countByCategory();

    // 按城市统计（city-count）
    List<Map<String, Object>> countByCity();

    // 月度趋势（monthly-trend）
    List<Map<String, Object>> monthlyTrend();

    // 你原来的两个方法保留
    List<DomesticProduct> getByCategory(String cnCategory);
    int countByCategory(String cnCategory);
}