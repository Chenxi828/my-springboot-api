package com.ecommerce.ecommerceanalysis.entity.domestic;

import java.math.BigDecimal;
import java.util.Date;

public class DomesticOrders {
    private Integer id;
    private String userId;
    private String userName;
    private String productId;
    private String productName;
    private String productCategory;
    private BigDecimal unitPrice;
    private Date purchaseTime;
    private Integer purchaseQuantity;
    private BigDecimal consumptionAmount;
    private String userCity;
    private String userGender;
    private Integer userAge;

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Date getPurchaseTime() { return purchaseTime; }
    public void setPurchaseTime(Date purchaseTime) { this.purchaseTime = purchaseTime; }

    public Integer getPurchaseQuantity() { return purchaseQuantity; }
    public void setPurchaseQuantity(Integer purchaseQuantity) { this.purchaseQuantity = purchaseQuantity; }

    public BigDecimal getConsumptionAmount() { return consumptionAmount; }
    public void setConsumptionAmount(BigDecimal consumptionAmount) { this.consumptionAmount = consumptionAmount; }

    public String getUserCity() { return userCity; }
    public void setUserCity(String userCity) { this.userCity = userCity; }

    public String getUserGender() { return userGender; }
    public void setUserGender(String userGender) { this.userGender = userGender; }

    public Integer getUserAge() { return userAge; }
    public void setUserAge(Integer userAge) { this.userAge = userAge; }
}