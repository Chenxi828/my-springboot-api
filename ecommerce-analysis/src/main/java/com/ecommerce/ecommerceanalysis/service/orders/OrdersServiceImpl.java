package com.ecommerce.ecommerceanalysis.service.orders;

import com.ecommerce.ecommerceanalysis.entity.Orders;
import com.ecommerce.ecommerceanalysis.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    @Cacheable(value = "orders", key = "'listAll'")
    public List<Orders> listAll() {
        return ordersMapper.listAll();
    }

    @Override
    public List<Orders> listAll(int page, int size) {
        int offset = (page - 1) * size;
        return ordersMapper.listAllWithPage(offset, size);
    }

    @Override
    @Cacheable(value = "statistics", key = "'categoryCount'")
    public List<Map<String, Object>> getCategoryCount() {
        return ordersMapper.countByCategory().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", item.getProductCategory());
                    map.put("value", item.getCount());
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "statistics", key = "'cityCount'")
    public List<Map<String, Object>> getCityCount() {
        return ordersMapper.countByCity().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", item.getUserCity());
                    map.put("value", item.getCount());
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "statistics", key = "'monthlyTrend'")
    public List<Map<String, Object>> getMonthlyTrend() {
        return ordersMapper.getMonthlyTrend().stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("month", item.getMonth());
                    map.put("total", item.getTotal());
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "statistics", key = "'totalOrders'")
    public Long getTotalOrders() {
        return ordersMapper.countTotal();
    }

    @Override
    @Cacheable(value = "statistics", key = "'totalAmount'")
    public Double getTotalAmount() {
        return ordersMapper.sumAmount();
    }

    @Override
    @Cacheable(value = "statistics", key = "'dateCount'")
    public List<Map<String, Object>> getDateCount() {
        return ordersMapper.getDateCount().stream()
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
            String ptCategory = convertToPTCategory(category);
            List<Map<String, Object>> list = ordersMapper.searchOrders(keyword, ptCategory, offset, size);
            enrichProductName(list);
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Long countSearchOrders(String keyword, String category) {
        try {
            String ptCategory = convertToPTCategory(category);
            return ordersMapper.countSearchOrders(keyword, ptCategory);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public Map<String, Object> getAmountStats(String keyword, String category) {
        try {
            String ptCategory = convertToPTCategory(category);
            return ordersMapper.getAmountStats(keyword, ptCategory);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("amount", 0);
            map.put("order_count", 0);
            return map;
        }
    }

    @Override
    public Map<String, Object> getBrazilOrderStats(String keyword) {
        Map<String, Object> stats = new HashMap<>();
        try {
            // 总订单数
            Long totalCount = ordersMapper.getTotalOrderCount(keyword);
            // 已完成订单数（状态为delivered）
            Long completedCount = ordersMapper.getCompletedOrderCount(keyword);
            // 总金额
            java.math.BigDecimal totalAmount = ordersMapper.getTotalOrderAmount(keyword);

            stats.put("totalCount", totalCount != null ? totalCount : 0L);
            stats.put("completedCount", completedCount != null ? completedCount : 0L);
            stats.put("totalAmount", totalAmount != null ? totalAmount : java.math.BigDecimal.ZERO);
        } catch (Exception e) {
            stats.put("totalCount", 0L);
            stats.put("completedCount", 0L);
            stats.put("totalAmount", java.math.BigDecimal.ZERO);
        }
        return stats;
    }

    // 中文品类 → 葡萄牙语品类映射
    private String convertToPTCategory(String category) {
        if (category == null || category.isEmpty() || "全部".equals(category)) return null;
        Map<String, String> map = new HashMap<>();
        map.put("电子产品", "eletronicos");
        map.put("服装", "moda_feminina");
        map.put("家居", "moveis_decoracao");
        map.put("美妆", "beleza_saude");
        map.put("运动", "esporte_lazer");
        map.put("家用电器", "eletrodomesticos");
        map.put("婴幼儿", "infantil");
        map.put("建材", "casa_construcao");
        map.put("宠物", "pet_shop");
        return map.getOrDefault(category, category);
    }

    // ==================== 巴西订单中文产品名映射 ====================

    // 葡萄牙语品类 → 中文产品名列表（根据 productId hashCode 分配，同一产品ID始终显示同一名字）
    private static final Map<String, String[]> CATEGORY_PRODUCTS = new HashMap<>();
    static {
        CATEGORY_PRODUCTS.put("eletronicos",       new String[]{"智能手机", "蓝牙耳机", "笔记本电脑", "智能手表", "平板电脑", "充电宝", "无线鼠标"});
        CATEGORY_PRODUCTS.put("eletrodomesticos",  new String[]{"冰箱", "洗衣机", "空调", "微波炉", "电饭煲", "吸尘器", "空气炸锅"});
        CATEGORY_PRODUCTS.put("moveis_decoracao",  new String[]{"沙发", "床", "餐桌", "书架", "椅子", "台灯", "收纳盒"});
        CATEGORY_PRODUCTS.put("beleza_saude",      new String[]{"护肤品", "化妆品", "香水", "洗发水", "护手霜", "面膜", "防晒霜"});
        CATEGORY_PRODUCTS.put("esporte_lazer",     new String[]{"运动鞋", "瑜伽垫", "健身器材", "户外帐篷", "跑步机", "运动水壶"});
        CATEGORY_PRODUCTS.put("moda_feminina",     new String[]{"连衣裙", "T恤", "牛仔裤", "外套", "裙子", "衬衫", "高跟鞋"});
        CATEGORY_PRODUCTS.put("moda_masculina",    new String[]{"衬衫", "T恤", "裤子", "外套", "鞋子", "皮带", "领带"});
        CATEGORY_PRODUCTS.put("infantil",          new String[]{"婴儿服装", "玩具", "婴儿车", "奶瓶", "纸尿裤", "儿童绘本"});
        CATEGORY_PRODUCTS.put("casa_construcao",   new String[]{"工具套装", "灯具", "五金", "涂料", "水管", "电钻"});
        CATEGORY_PRODUCTS.put("pet_shop",          new String[]{"狗粮", "猫粮", "宠物玩具", "宠物窝", "宠物零食"});
        CATEGORY_PRODUCTS.put("alimentos",         new String[]{"饼干", "咖啡", "巧克力", "坚果", "调味料", "果酱"});
        CATEGORY_PRODUCTS.put("bebes",             new String[]{"婴儿奶粉", "纸尿裤", "婴儿湿巾", "奶瓶", "婴儿服装"});
        CATEGORY_PRODUCTS.put("papelaria",         new String[]{"笔记本", "文具套装", "签字笔", "文件夹", "便签纸"});
        CATEGORY_PRODUCTS.put("livros_interesse",  new String[]{"小说", "教材", "杂志", "儿童图书", "工具书"});
        CATEGORY_PRODUCTS.put("automotivo",        new String[]{"车载充电器", "汽车坐垫", "行车记录仪", "汽车香水", "雨刷"});
        CATEGORY_PRODUCTS.put("brinquedos",        new String[]{"积木", "遥控车", "毛绒玩具", "拼图", "桌游"});
        CATEGORY_PRODUCTS.put("telefonia",         new String[]{"手机壳", "手机膜", "手机支架", "充电器", "数据线"});
        CATEGORY_PRODUCTS.put("informatica_acessorios", new String[]{"鼠标垫", "键盘", "显示器", "U盘", "硬盘", "耳机"});
        CATEGORY_PRODUCTS.put("consoles_games",    new String[]{"游戏手柄", "游戏光盘", "主机支架", "耳机", "键盘"});
        CATEGORY_PRODUCTS.put("relogios_presentes", new String[]{"手表", "项链", "戒指", "手链", "耳环"});
    }

    /**
     * 根据巴西订单的葡萄牙语品类+productId，填充中文产品名
     * 保证同一 productId 始终映射到同一产品名
     */
    private void enrichProductName(List<Map<String, Object>> orders) {
        for (Map<String, Object> item : orders) {
            String cat = (String) item.get("category");
            Object productIdObj = item.get("productId");
            String productId = productIdObj != null ? productIdObj.toString() : "";

            String[] products = cat != null ? CATEGORY_PRODUCTS.get(cat) : null;
            String productName;
            if (products != null && products.length > 0) {
                int idx = productId.isEmpty() ? 0 : Math.abs(productId.hashCode()) % products.length;
                productName = products[idx];
            } else if (cat != null) {
                productName = cat + "商品";
            } else {
                productName = "未知产品";
            }

            item.put("product", productName);
            item.put("productName", productName);
        }
    }
}