package com.ai.mode.school.config.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/api/**") // 拦截所有 /api 接口
                .excludePathPatterns("/**"); // 排除登录接口
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有接口
                .allowedOriginPatterns("http://localhost:*") // 允许本地不同端口
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowCredentials(true) // 允许 Cookie
                .allowedHeaders("*")
                .maxAge(3600);
    }
}