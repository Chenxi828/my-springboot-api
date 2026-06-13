package com.ecommerce.ecommerceanalysis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据大屏 Mapper 接口
 */
@Mapper
public interface DashboardMapper {

    /**
     * 巴西订单总数
     */
    int getBrazilOrdersCount();

    /**
     * 巴西销售总额
     */
    double getBrazilOrdersAmount();

    /**
     * 国内订单总数
     */
    int getDomesticOrdersCount();

    /**
     * 国内销售总额
     */
    double getDomesticOrdersAmount();

    /**
     * 巴西城市分布（前10）
     */
    List<Map<String, Object>> getBrazilCityDistribution();

    /**
     * 国内城市分布（前10）
     */
    List<Map<String, Object>> getDomesticCityDistribution();

    /**
     * 巴西订单状态统计
     */
    List<Map<String, Object>> getBrazilOrderStatus();

    /**
     * 国内订单状态统计
     */
    List<Map<String, Object>> getDomesticOrderStatus();
}