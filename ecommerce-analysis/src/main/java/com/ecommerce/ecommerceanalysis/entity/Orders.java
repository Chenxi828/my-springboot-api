package com.ecommerce.ecommerceanalysis.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Orders {
    private Long id;
    private String orderId;          // 订单ID
    private String customerId;       // 用户ID
    private String orderStatus;      // 订单状态
    private LocalDateTime orderPurchaseTimestamp; // 下单时间
    private String productCategory;  // 商品类别
    private String paymentType;      // 支付方式
    private String customerCity;     // 用户城市
    private Double amount;           // 单价
    private Integer quantity;        // 数量
    private String category;         // 类目名称
}