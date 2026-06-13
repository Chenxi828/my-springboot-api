package com.ecommerce.ecommerceanalysis.config;

import com.ecommerce.ecommerceanalysis.entity.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT 认证过滤器
 * 拦截所有请求，验证 Token 的有效性
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 不需要验证 Token 的路径
     */
    private static final String[] EXCLUDED_PATHS = {
            "/api/user/login",
            "/api/user/logout",
            "/api/user/info",
            "/api/user/list",
            "/api/user/add",
            "/api/user/update",
            "/api/user/delete/",
            "/api/user/statistics/",
            "/api/dashboard/",
            "/api/orders/",
            "/api/domestic/",
            "/api/selection/",
            "/api/supply/",
            "/api/profit/",
            "/api/deepseek/",
            "/api/cache/",
            "/error"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 检查是否是不需要验证的路径
        String requestUri = request.getRequestURI();
        if (isExcludedPath(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从请求头中获取 Token
        String token = getJwtFromRequest(request);

        // 验证 Token
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // Token 有效，继续处理请求
            filterChain.doFilter(request, response);
        } else {
            // Token 无效或缺失，返回 401 错误
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            Result<String> result = new Result<>();
            result.setCode(401);
            result.setMsg("Token 无效或已过期，请重新登录");

            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(result));
            writer.flush();
            writer.close();
        }
    }

    /**
     * 从请求头中提取 JWT Token
     *
     * @param request HTTP 请求
     * @return JWT Token（不含 Bearer 前缀）
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }

    /**
     * 判断路径是否需要排除验证
     *
     * @param requestUri 请求路径
     * @return 是否排除
     */
    private boolean isExcludedPath(String requestUri) {
        for (String excludedPath : EXCLUDED_PATHS) {
            if (requestUri.startsWith(excludedPath)) {
                return true;
            }
        }
        return false;
    }
}
