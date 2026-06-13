package com.ecommerce.ecommerceanalysis.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

@Mapper
public interface CategoryMappingMapper {
    Map<String, Object> getByBr(String brCategory);
}