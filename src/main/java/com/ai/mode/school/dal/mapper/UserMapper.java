package com.ai.mode.school.dal.mapper;

import com.ai.mode.school.beans.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 自定义查询示例（可根据需求扩展）
    List<User> selectByUsername(@Param("username") String username);
}