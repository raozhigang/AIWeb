package com.ai.mode.school.service;

import cn.hutool.crypto.digest.BCrypt;
import com.ai.mode.school.beans.entity.User;
import com.ai.mode.school.common.exception.BusinessException;
import com.ai.mode.school.dal.mapper.UserMapper;
import com.ai.mode.school.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserLoginService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private HttpServletRequest request;

    // 用户登录核心逻辑
    public String login(String username, String password) {
        // 1. 查询用户是否存在
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null || !password.equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        // 2. 生成 JWT Token
        return JwtUtils.generateToken(user.getUsername());
    }

    public User getUser() {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(401,"未认证身份信息!");
        }
        if (!JwtUtils.validateToken(token)) {
            throw new BusinessException(401,"token失效!");
        }
        String userName = JwtUtils.parseToken(token);
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", userName));
        if(user == null){
            throw new BusinessException(500,"用户不存在!");
        }
        return user;
    }
}