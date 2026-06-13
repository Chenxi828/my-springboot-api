package com.ecommerce.ecommerceanalysis.entity.domestic;

public class DomesticCategoryCount {
    private String productCategory;
    private Integer count;

    // 必须的无参构造
    public DomesticCategoryCount() {}

    // Getter/Setter
    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}