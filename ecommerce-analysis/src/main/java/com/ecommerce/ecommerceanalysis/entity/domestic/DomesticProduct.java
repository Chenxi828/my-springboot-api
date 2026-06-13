package com.ecommerce.ecommerceanalysis.entity.domestic;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DomesticProduct {
    private Integer id;
    private String productName;
    private String category;
    private BigDecimal supplyPrice;
    private BigDecimal grossRate;
    private Integer supplierRating;
    private String supplierContact;
    private String userId;
    private String userName;
    private String city;
    private String gender;
    private Integer age;
    private Date buyTime;
    private Integer quantity;
    private BigDecimal totalAmount;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getSupplyPrice() { return supplyPrice; }
    public void setSupplyPrice(BigDecimal supplyPrice) { this.supplyPrice = supplyPrice; }
    public BigDecimal getGrossRate() { return grossRate; }
    public void setGrossRate(BigDecimal grossRate) { this.grossRate = grossRate; }
    public Integer getSupplierRating() { return supplierRating; }
    public void setSupplierRating(Integer supplierRating) { this.supplierRating = supplierRating; }
    public String getSupplierContact() { return supplierContact; }
    public void setSupplierContact(String supplierContact) { this.supplierContact = supplierContact; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Date getBuyTime() { return buyTime; }
    public void setBuyTime(Date buyTime) { this.buyTime = buyTime; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}