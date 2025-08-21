package com.ai.mode.school.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

// Calligraphy.java
@Data
@TableName("calligraphy")
public class Calligraphy {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String userName;
    
    @TableField("author")
    private String author;
    
    @TableField("name")
    private String name;
    
    @TableField("title")
    private String title;
    
    @TableField("description")
    private String description;
    
    @TableField("image_url")
    private String imageUrl;
    
    @TableField("image_short_url")
    private String imageShortUrl;
    
    @TableField("style_type")
    private String styleType;
    
    @TableField("source")
    private String source;
    
    @TableField("view_count")
    private Integer viewCount;
    
    @TableField("like_count")
    private Integer likeCount;
    
    @TableField("is_public")
    private Boolean isPublic;
    
    private Date createdAt;
    
    private Date updatedAt;
}

