package com.ai.mode.school.dal.mapper;

import com.ai.mode.school.beans.entity.Calligraphy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface CalligraphyMapper extends BaseMapper<Calligraphy> {
    // 示例：按用户ID分页查询作品
    IPage<Calligraphy> selectByUserId(Page<?> page, @Param("userId") Long userId);
}

