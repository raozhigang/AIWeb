package com.ai.mode.school.controller;

import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.service.UserLoginService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    private UserLoginService loginService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        String token = loginService.login(request.getUsername(), request.getPassword());
        return Result.success(token);
    }

    // 登录请求DTO
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}