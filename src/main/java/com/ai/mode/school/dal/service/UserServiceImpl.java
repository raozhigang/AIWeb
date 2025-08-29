package com.ai.mode.school.dal.service;

import com.ai.mode.school.beans.dto.UserDto;
import com.ai.mode.school.beans.entity.User;
import com.ai.mode.school.common.exception.BusinessException;
import com.ai.mode.school.dal.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

// UserServiceImpl.java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> {

    //默认密码123
    private final static String password = "123";

    public User getUser(String userName){
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getUsername, userName);
        return this.getOne(lambdaQueryWrapper);
    }

    /**
     * 创建用户
     * @param user 用户对象
     * @return 插入成功返回true，失败返回false
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(User user) {
        User useEntity = this.getUser(user.getUsername());
        if(useEntity!=null){
            throw new BusinessException("用户已存在!");
        }
        if(StringUtils.isEmpty(user.getPassword())){
            user.setPassword(password);
        }
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
