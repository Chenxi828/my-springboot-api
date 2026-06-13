package com.ecommerce.ecommerceanalysis.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String email;
    private String status;
    private String lastLogin;
}

