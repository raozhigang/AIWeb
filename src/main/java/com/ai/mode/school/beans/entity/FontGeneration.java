package com.ai.mode.school.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

// FontGeneration.java
@Data
@TableName("font_generation")
public class FontGeneration {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String userName;
    
    @TableField("input_text")
    private String inputText;
    
    @TableField("input_image_url")
    private String inputImageUrl;
    
    @TableField("style_type")
    private String styleType;
    
    @TableField("result_image_url")
    private String resultImageUrl;
    
    @TableField("status")
    private String status;
    
    private Date createdAt;
    
    private Date updatedAt;
}

