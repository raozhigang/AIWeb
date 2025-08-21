package com.ai.mode.school.config.auth;

import com.ai.mode.school.common.exception.BusinessException;
import com.ai.mode.school.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PermissionInterceptor implements HandlerInterceptor {
    @Resource
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUri = request.getRequestURI(); // 获取请求路径
        System.out.println("拦截器 preHandle 触发，请求路径：" + requestUri); // 打印日志
        // 1. 从请求头获取 Token
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new BusinessException(401,"未认证身份信息!");
        }

        // 2. 验证 Token 有效性
        if (!JwtUtils.validateToken(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new BusinessException(401,"token失效!");
        }

        // 3. 解析用户ID
        String userName = jwtUtils.parseToken(token);

        // 4. 获取方法上的权限注解
        /*HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresPermission requiresPermission = handlerMethod.getMethodAnnotation(RequiresPermission.class);
        if (requiresPermission == null) {
            return true; // 无权限要求，直接放行
        }*/
        // 5. 检查用户是否拥有权限（简易版：假设所有登录用户都有权限）
        // 实际场景可扩展：查询用户权限表，判断是否包含 requiresPermission.value()
        return true;
    }
}