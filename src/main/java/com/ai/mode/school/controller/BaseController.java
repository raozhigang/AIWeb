package com.ai.mode.school.controller;

import com.ai.mode.school.beans.entity.User;
import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.service.UserLoginService;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class BaseController {
    @Resource
    private UserLoginService loginService;

    /**
     * 从请求头中解析JWT,获取用户信息
     *
     * @return
     */
    public User getUser() {
        return loginService.getUser();
    }
}