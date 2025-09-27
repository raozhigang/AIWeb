package com.ai.mode.school.dal.service;

import com.ai.mode.school.beans.dto.UserGenerateReq;
import com.ai.mode.school.beans.entity.FontGeneration;
import com.ai.mode.school.common.exception.BusinessException;
import com.ai.mode.school.dal.mapper.FontGenerationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// FontGenerationServiceImpl.java
@Service
public class FontGenerationServiceImpl extends ServiceImpl<FontGenerationMapper, FontGeneration>{

    /**
     * 分页查询字体生成记录（支持条件过滤）
     * @return 分页结果（包含数据和总条数）
     */
    public IPage<FontGeneration> listFontGenerationsByPage(UserGenerateReq req) {
        Page<FontGeneration> page = new Page<>(req.getPageNum(), req.getPageSize());
        LambdaQueryWrapper<FontGeneration> queryWrapper = Wrappers.lambdaQuery();

        // 动态拼接过滤条件
        if (StringUtils.isNotBlank(req.getUserName())) {
            queryWrapper.like(FontGeneration::getUserName, req.getUserName());
        }
        return page(page, queryWrapper);
    }

    public FontGeneration getByStyleName(String userName,String styleName) {
        LambdaQueryWrapper<FontGeneration> queryWrapper = Wrappers.lambdaQuery();
        // 动态拼接过滤条件
        queryWrapper.eq(FontGeneration::getUserName, userName);
        queryWrapper.eq(FontGeneration::getStyleName, styleName);
        return getOne(queryWrapper);
    }
    /**
     * 保存字体生成记录（新增）
     * @return 保存成功返回 true，失败返回 false
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFontGeneration(FontGeneration fontGeneration) {
        // 可选：添加业务校验（如必填字段检查）
        if (fontGeneration.getUserName() == null || fontGeneration.getOperateType() == null) {
            throw new BusinessException("参数错误:操作用户为空");
        }
        return save(fontGeneration);
    }

    public boolean updateFontGeneration(String batchNo,String styleName) {

        // 可选：添加业务校验（如必填字段检查）
        LambdaQueryWrapper<FontGeneration> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(FontGeneration::getBatchNo,batchNo);
        FontGeneration fontGeneration = this.getOne(queryWrapper);
        if(fontGeneration == null){
            throw new BusinessException("不存在该操作记录");
        }
        fontGeneration.setStyleName(styleName);
        FontGeneration byStyleName = this.getByStyleName(fontGeneration.getUserName(), fontGeneration.getStyleName());
        if(byStyleName!=null){
            throw new BusinessException("风格名称已经存在");
        }
        return updateById(fontGeneration);
    }
}
