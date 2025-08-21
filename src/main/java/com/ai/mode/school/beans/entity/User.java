package com.ai.mode.school.beans.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

// User.java
@Data
@TableName("user")
public class User{
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("username")
    private String username;
    
    @TableField("password")
    private String password;
    
    @TableField("mobile")
    private String mobile;
    
    @TableField("wechat")
    private String wechat;
    
    @TableField("email")
    private String email;
    
    @TableField("gender")
    private String gender;
    
    @TableField("avatar")
    private String avatar;
    
    @TableField("nickname")
    private String nickname;
    
    @TableField("style_preference")
    private String stylePreference;
    
    @TableField("status")
    private Integer status;
    
    private Date createdAt;
    
    private Date updatedAt;
}
