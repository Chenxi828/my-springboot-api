package com.ecommerce.ecommerceanalysis.mapper;

import com.ecommerce.ecommerceanalysis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {
    List<User> list();
    void add(User user);
    void update(User user);
    void delete(Integer id);
}