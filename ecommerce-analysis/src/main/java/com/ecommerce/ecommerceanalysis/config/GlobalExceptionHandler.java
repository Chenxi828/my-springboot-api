package com.ecommerce.ecommerceanalysis.config;

import com.ecommerce.ecommerceanalysis.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 作用：
 * 1. 接口报错时返回友好的 JSON 提示，而不是直接抛出异常堆栈
 * 2. 统一项目错误处理，让代码更规范，答辩更专业
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获所有异常
     * @param e 异常对象
     * @return 统一格式的错误结果
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        // 用你 Result 类里的 fail 方法来返回错误
        return Result.fail(500, "系统异常：" + e.getMessage());
    }
}