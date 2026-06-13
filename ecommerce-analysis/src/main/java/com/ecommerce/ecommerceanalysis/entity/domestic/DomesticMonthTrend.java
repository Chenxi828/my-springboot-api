package com.ecommerce.ecommerceanalysis.entity.domestic;

public class DomesticMonthTrend {
    private String month;
    private Double total;

    public DomesticMonthTrend() {}

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}