package com.ecommerce.ecommerceanalysis.entity.domestic;

public class DomesticCityCount {
    private String userCity;
    private Integer count;

    public DomesticCityCount() {}

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}