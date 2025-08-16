package com.ai.mode.school.controller;

import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.service.ModelClientService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@CrossOrigin(
        origins = "http://localhost:9020",  // ✅ 不能用 "*"
        allowCredentials = "true"           // ✅ 允许凭据
)
public class ModelController {

    @Resource
    private ModelClientService modelClientService;

    /**
     * 接收图片并调用 model 检测服务
     * @param imageFile 上传的图片文件
     * @return 检测结果（包含状态码、消息、数据）
     */
    @PostMapping("/uploadImage")
    public Result detect(@RequestParam("image") MultipartFile imageFile) {
            // 调用 model 客户端服务
        JSONObject jsonObject = modelClientService.detectObjects(imageFile);
        return Result.success(jsonObject);

    }
}