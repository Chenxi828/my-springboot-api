package com.ecommerce.ecommerceanalysis.service.dashboard;

import com.ecommerce.ecommerceanalysis.mapper.DashboardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Resource
    private DashboardMapper dashboardMapper;

    @Override
    public Map<String, Object> getOverview() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int brazilOrders = dashboardMapper.getBrazilOrdersCount();
            double brazilAmount = dashboardMapper.getBrazilOrdersAmount();
            int chinaOrders = dashboardMapper.getDomesticOrdersCount();
            double chinaAmount = dashboardMapper.getDomesticOrdersAmount();

            result.put("brazilOrders", brazilOrders);
            result.put("brazilAmount", brazilAmount);
            result.put("chinaOrders", chinaOrders);
            result.put("chinaAmount", chinaAmount);
            result.put("totalOrders", brazilOrders + chinaOrders);
            result.put("totalAmount", brazilAmount + chinaAmount);
            result.put("timestamp", new Date().toString());

        } catch (Exception e) {
            logger.error("获取概览数据失败", e);
            result.put("brazilOrders", 0);
            result.put("brazilAmount", 0);
            result.put("chinaOrders", 0);
            result.put("chinaAmount", 0);
            result.put("totalOrders", 0);
            result.put("totalAmount", 0);
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getCityStats() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> brazilCities = dashboardMapper.getBrazilCityDistribution();
            List<Map<String, Object>> chinaCities = dashboardMapper.getDomesticCityDistribution();

            result.put("brazil", brazilCities != null ? brazilCities : Collections.emptyList());
            result.put("china", chinaCities != null ? chinaCities : Collections.emptyList());

        } catch (Exception e) {
            logger.error("获取城市分布数据失败", e);
            result.put("brazil", Collections.emptyList());
            result.put("china", Collections.emptyList());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getStatusStats() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> brazilStatus = dashboardMapper.getBrazilOrderStatus();
            List<Map<String, Object>> domesticStatus = dashboardMapper.getDomesticOrderStatus();

            result.put("brazilStatus", brazilStatus != null ? brazilStatus : Collections.emptyList());
            result.put("domesticStatus", domesticStatus != null ? domesticStatus : Collections.emptyList());
            result.put("totalStatus", brazilStatus != null ? brazilStatus : Collections.emptyList());

        } catch (Exception e) {
            logger.error("获取订单状态数据失败", e);
            result.put("brazilStatus", Collections.emptyList());
            result.put("domesticStatus", Collections.emptyList());
            result.put("totalStatus", Collections.emptyList());
        }
        
        return result;
    }
}