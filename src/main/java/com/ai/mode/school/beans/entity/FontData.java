package com.ai.mode.school.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;
// FontData.java
@Data
@TableName("font_data")
public class FontData {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("font_value")
    private String fontValue;
    
    @TableField("image_abs_url")
    private String imageAbsUrl;
    
    @TableField("image_remote_url")
    private String imageRemoteUrl;
    
    @TableField("style_type")
    private String styleType;
    
    private Date createdAt;
    
    private Date updatedAt;
}