package com.ai.mode.school.dal.service;

import com.ai.mode.school.beans.entity.User;
import com.ai.mode.school.dal.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// UserServiceImpl.java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> {

    public void getUser(String userName){
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, userName);
        this.getOne(lambdaQueryWrapper);
    }

    /**
     * 根据用户名模糊查询
     * @param username 用户名
     * @return 用户列表
     */
    public List<User> listUsersByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StringUtils.isNotBlank(username), User::getUsername, username);
        return list(queryWrapper);
    }

    /**
     * 创建用户
     * @param user 用户对象
     * @return 插入成功返回true，失败返回false
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(User user) {
        return save(user);
    }

    /**
     * 更新用户信息
     * @param user 用户对象（需包含ID）
     * @return 更新成功返回true，失败返回false
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        return updateById(user);
    }


}
