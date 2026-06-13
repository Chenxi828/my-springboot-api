package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.entity.Result;
import com.ecommerce.ecommerceanalysis.mapper.DomesticOrdersMapper;
import com.ecommerce.ecommerceanalysis.mapper.OrdersMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 补充统计接口 —— 仅保留独有的接口
 * 巴西/国内订单相关已迁移到 OrdersController / DomesticOrdersController
 * 使用 MyBatis 替代原始 JDBC
 */
@RestController
@RequestMapping("/api")
public class DataStatisticsController {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private DomesticOrdersMapper domesticOrdersMapper;

    // ==================== 用户补充接口 ====================

    /**
     * 用户信息
     * GET /api/user/info
     */
    @GetMapping("/user/info")
    public Result<Map<String, Object>> userInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("username", "admin");
        info.put("role", "admin");
        info.put("nickname", "超级管理员");
        info.put("avatar", "");
        return Result.success(info);
    }

    // ==================== 选品对比接口（MyBatis 版） ====================

    /**
     * 选品对比（兼容前端）
     * GET /api/selection/compare
     */
    @GetMapping("/selection/compare")
    public Result<Map<String, Object>> selectionCompare() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 巴西品类数据 — 直接从 MyBatis 的 countByCategory 获取
            List<Map<String, Object>> brazil = new ArrayList<>();
            ordersMapper.countByCategory().forEach(dcc -> {
                Map<String, Object> item = new HashMap<>();
                item.put("name", dcc.getProductCategory());
                item.put("value", dcc.getCount());
                brazil.add(item);
            });

            // 国内品类数据
            List<Map<String, Object>> china = domesticOrdersMapper.countByCategoryForMap();

            result.put("brazil", brazil);
            result.put("china", china);
        } catch (Exception e) {
            result.put("brazil", Collections.emptyList());
            result.put("china", Collections.emptyList());
        }
        return Result.success(result);
    }

    // ==================== 供应链接口（MyBatis 版） ====================

    /**
     * 供应链列表（兼容前端）
     * GET /api/supply/list
     */
    @GetMapping("/supply/list")
    public Result<List<Map<String, Object>>> supplyList() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            list = domesticOrdersMapper.getSupplyList();
        } catch (Exception ignored) {}
        return Result.success(list);
    }
}
