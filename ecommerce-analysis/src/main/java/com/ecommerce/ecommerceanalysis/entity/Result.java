package com.ecommerce.ecommerceanalysis.entity;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    // 无参构造器（给Lombok和框架用）
    public Result() {}

    // 全参构造器
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功返回（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "请求成功", data);
    }

    // 成功返回（不带数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "请求成功", null);
    }

    // 失败返回（给异常处理器用）
    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }
}
