package com.ecommerce.ecommerceanalysis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ecommerce.ecommerceanalysis.mapper")
public class EcommerceAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceAnalysisApplication.class, args);
    }

}
