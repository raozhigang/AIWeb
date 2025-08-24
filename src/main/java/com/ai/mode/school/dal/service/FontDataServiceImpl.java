package com.ai.mode.school.dal.service;

import com.ai.mode.school.beans.entity.FontData;
import com.ai.mode.school.dal.mapper.FontDataMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// FontDataServiceImpl.java
@Service
public class FontDataServiceImpl extends ServiceImpl<FontDataMapper, FontData> {

    /**
     * 根据字体名称模糊查询
     * @param fontValue 字体名称
     * @return 字体数据列表
     */
    public List<FontData> listFontsByValue(String fontValue,String styleType) {
        LambdaQueryWrapper<FontData> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(FontData::getFontValue, fontValue);
        queryWrapper.eq(FontData::getStyleType, styleType);
        return list(queryWrapper);
    }

    /**
     * 根据字体风格查询
     * @param fontStyle 字体风格（如行书、楷体）
     * @return 字体数据列表
     */
    public List<FontData> listFontsByStyle(String fontStyle) {
        return list(Wrappers.<FontData>lambdaQuery()
                .eq(StringUtils.isNotBlank(fontStyle), FontData::getStyleType, fontStyle));
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean saveFontData(FontData fontData) {
        return save(fontData);
    }

    /**
     * 批量插入字体数据
     * @param fontDataList 字体数据列表
     * @return 插入成功条数
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInsert(List<FontData> fontDataList) {
        return this.saveBatch(fontDataList);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateFontData(FontData fontData) {
        return updateById(fontData);
    }

}
