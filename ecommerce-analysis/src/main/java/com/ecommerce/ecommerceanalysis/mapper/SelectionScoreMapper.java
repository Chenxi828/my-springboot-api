package com.ecommerce.ecommerceanalysis.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

@Mapper
public interface SelectionScoreMapper {
    void insert(String category, int market, int supply, int finalScore, String grade);
    void update(String category, int market, int supply, int finalScore, String grade);
    Map<String, Object> getByCategory(String category);
}