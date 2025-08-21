package com.ai.mode.school.controller.business;

import com.ai.mode.school.dal.service.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

// UserController.java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Resource
    private UserServiceImpl userService;
}

