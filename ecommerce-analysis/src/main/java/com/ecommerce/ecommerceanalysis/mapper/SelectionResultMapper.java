package com.ecommerce.ecommerceanalysis.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface SelectionResultMapper {
    void insert(String cnCategory, String brCategory, int demandScore, int growthScore, int competitionScore, int profitScore, int totalScore, String reason);
    void update(String cnCategory, int demandScore, int growthScore, int competitionScore, int profitScore, int totalScore, String reason);
    Map<String, Object> getByCategory(String cnCategory);
    List<Map<String, Object>> getAll();
}
