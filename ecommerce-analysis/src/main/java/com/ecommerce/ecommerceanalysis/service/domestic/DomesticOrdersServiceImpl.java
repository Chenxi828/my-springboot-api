package com.ecommerce.ecommerceanalysis.service.domestic;

import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticProduct;
import com.ecommerce.ecommerceanalysis.entity.domestic.DomesticDateCount;
import com.ecommerce.ecommerceanalysis.mapper.DomesticOrdersMapper;
import com.ecommerce.ecommerceanalysis.mapper.DomesticProductsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class DomesticOrdersServiceImpl implements DomesticOrdersService {

    @Autowired
    private DomesticProductsMapper domesticProductsMapper;

    @Autowired
    private DomesticOrdersMapper domesticOrdersMapper;

    @Override
    public List<DomesticProduct> listAll() {
        return domesticProductsMapper.listAll();
    }

    @Override
    public List<Map<String, Object>> getCategoryCount() {
        // 修复：使用 domesticOrdersMapper 查询 domestic_orders 表的品类统计
        return domesticOrdersMapper.countByCategoryForMap();
    }

    @Override
    public List<Map<String, Object>> getCityCount() {
        // 修复：使用 domesticOrdersMapper 查询 domestic_orders 表的城市统计
        return domesticOrdersMapper.countByCity().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", item.getUserCity());
                    map.put("value", item.getCount());
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getMonthlyTrend() {
        // 修复：使用 domesticOrdersMapper 查询 domestic_orders 表的月度趋势
        return domesticOrdersMapper.getMonthlyTrend().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("month", item.getMonth());
                    map.put("total", item.getTotal());
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> countAllByCategory() {
        return domesticProductsMapper.countByCategory();
    }

    @Override
    public Long getTotalOrders() {
        return domesticOrdersMapper.countTotal();
    }

    @Override
    public Double getTotalAmount() {
        return domesticOrdersMapper.sumAmount();
    }

    @Override
    public List<Map<String, Object>> getOrderList() {
        return domesticOrdersMapper.listAll().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", item.getId());
                    map.put("productName", item.getProductName());
                    map.put("productCategory", item.getProductCategory());
                    map.put("consumptionAmount", item.getConsumptionAmount());
                    map.put("purchaseTime", item.getPurchaseTime());
                    map.put("userCity", item.getUserCity());
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getDateCount() {
        return domesticOrdersMapper.getDateCount().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("date", item.getDate());
                    map.put("count", item.getCount());
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> searchOrders(String keyword, String category, int page, int size) {
        try {
            int offset = (page - 1) * size;
            return domesticOrdersMapper.searchOrders(keyword, category, offset, size);
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public Long countSearchOrders(String keyword, String category) {
        try {
            return domesticOrdersMapper.countSearchOrders(keyword, category);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public Map<String, Object> getAmountStats(String keyword, String category) {
        try {
            return domesticOrdersMapper.getAmountStats(keyword, category);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("amount", 0);
            map.put("order_count", 0);
            return map;
        }
    }

    @Override
    public List<Map<String, Object>> countByCategoryForMap() {
        try {
            return domesticOrdersMapper.countByCategoryForMap();
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }
}