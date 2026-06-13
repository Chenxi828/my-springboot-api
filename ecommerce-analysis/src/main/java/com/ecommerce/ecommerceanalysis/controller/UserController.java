package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.config.JwtTokenProvider;
import com.ecommerce.ecommerceanalysis.entity.Result;
import com.ecommerce.ecommerceanalysis.entity.User;
import com.ecommerce.ecommerceanalysis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户登录、退出控制器
 * 集成 JWT 认证机制
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录接口
     * 地址：POST /api/user/login
     * 账号：admin  密码：123456
     * 作用：实现系统登录验证，返回 JWT Token
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(
            @RequestBody User user) {

        String username = user.getUsername();
        String password = user.getPassword();
        
        // 固定账号密码校验
        if ("admin".equals(username) && "123456".equals(password)) {
            // 使用 JWT 生成真正的 Token
            String token = jwtTokenProvider.generateToken(username, "ADMIN");

            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("username", username);
            map.put("role", "ADMIN");
            map.put("expireTime", jwtTokenProvider.getExpiration());

            return Result.success(map);
        }

        // 登录失败
        Result<Map<String, Object>> result = new Result<>();
        result.setCode(401);
        result.setMsg("账号或密码错误");
        return result;
    }

    /**
     * 退出登录接口
     * 地址：POST /api/user/logout
     * 作用：清空登录状态，退出系统
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("退出登录成功");
    }

    /**
     * 获取用户信息接口（需要Token认证）
     * 地址：GET /api/user/detail
     */
    @GetMapping("/detail")
    public Result<Map<String, String>> getUserDetail(
            @RequestHeader("Authorization") String authorization) {

        // 解析 Token
        String token = authorization.substring(7); // 移除 "Bearer " 前缀
        String username = jwtTokenProvider.getUsernameFromToken(token);
        String role = jwtTokenProvider.getRoleFromToken(token);

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("role", role);

        return Result.success(map);
    }

    // ===================== 用户 CRUD 接口 =====================

    /**
     * 获取用户列表
     * 地址：GET /api/user/list
     */
    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userMapper.list());
    }

    /**
     * 新增用户
     * 地址：POST /api/user/add
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userMapper.add(user);
        return Result.success();
    }

    /**
     * 修改用户
     * 地址：PUT /api/user/update
     */
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userMapper.update(user);
        return Result.success();
    }

    /**
     * 删除用户
     * 地址：DELETE /api/user/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        userMapper.delete(id);
        return Result.success();
    }

    /**
     * 用户统计信息
     * 地址：GET /api/user/statistics/user/info
     */
    @GetMapping("/statistics/user/info")
    public Result<Map<String, Object>> userStatistics() {
        Map<String, Object> stats = new HashMap<>();
        List<User> allUsers = userMapper.list();
        stats.put("totalUsers", allUsers.size());
        stats.put("activeUsers", allUsers.stream().filter(u -> "active".equals(u.getStatus())).count());
        stats.put("roleDistribution", allUsers.stream()
                .collect(java.util.stream.Collectors.groupingBy(User::getRole, java.util.stream.Collectors.counting())));
        stats.put("timestamp", System.currentTimeMillis());
        return Result.success(stats);
    }
}