package com.ai.mode.school.controller.business;

import cn.hutool.core.bean.BeanUtil;
import com.ai.mode.school.beans.dto.UserDto;
import com.ai.mode.school.beans.entity.Calligraphy;
import com.ai.mode.school.beans.entity.User;
import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.controller.BaseController;
import com.ai.mode.school.dal.service.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

// UserController.java
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    @Resource
    private UserServiceImpl userService;

    @PostMapping("/create")
    public Result<String> create(@RequestBody User user) {
        userService.saveUser(user);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success();
    }

    @PostMapping("/getUserInfo")
    public Result<UserDto> getUserInfo() {
        User user = getUser();
        UserDto userDto =new UserDto();
        BeanUtil.copyProperties(user,userDto);
        return Result.success(userDto);
    }

    @GetMapping("/user/{userId}")
    public Result<List<Calligraphy>> getUserWorks(@PathVariable Long userId) {
        return Result.success();
    }
}

