package com.ecommerce.ecommerceanalysis.service.selection;

import java.util.List;
import java.util.Map;

public interface SelectionService {
    Map<String, Object> recommend(int top);
    Map<String, Object> score(String category);
    List<Map<String, Object>> blueOcean();
    List<Map<String, Object>> supplyMatch(String category);
    Map<String, Object> profitCalc(Map<String, Object> params);
    Map<String, Object> compare(List<String> categories);
}