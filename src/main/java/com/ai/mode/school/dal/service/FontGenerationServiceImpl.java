package com.ai.mode.school.dal.service;

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
     * @param pageNum  当前页码（从 1 开始）
     * @param pageSize 每页显示数量（如 10、20）
     * @param fontStyle 字体样式（可选过滤条件）
     * @return 分页结果（包含数据和总条数）
     */
    public IPage<FontGeneration> listFontGenerationsByPage(int pageNum, int pageSize,
                                                           String userName, String fontStyle) {
        Page<FontGeneration> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<FontGeneration> queryWrapper = Wrappers.lambdaQuery();

        // 动态拼接过滤条件
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like(FontGeneration::getUserName, userName);
        }
        return page(page, queryWrapper);
    }

    /**
     * 保存字体生成记录（新增）
     * @return 保存成功返回 true，失败返回 false
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFontGeneration(String userName,String inputImageUrl) {
        // 可选：添加业务校验（如必填字段检查）
        if (userName == null || inputImageUrl == null) {
            throw new BusinessException("参数为空:输入图片地址");
        }
        FontGeneration fontGeneration =new FontGeneration();
        fontGeneration.setUserName(userName);
        fontGeneration.setInputImageUrl(inputImageUrl);
        return save(fontGeneration);
    }
}
