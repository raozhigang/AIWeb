package com.ai.mode.school.service;

import com.ai.mode.school.common.exception.BusinessEnum;
import com.ai.mode.school.common.exception.BusinessException;
import com.ai.mode.school.utils.ImageUploadUtils;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class ModelClientService {

    @Resource
    private RestTemplate restTemplate;

    private final String modelServiceUrl = "url";


    /**
     * 调用 model 推理服务，上传图片并获取检测结果
     * @param imageFile 上传的图片文件（MultipartFile）
     * @return 检测结果（Map 结构）
     * @throws IOException 文件读取异常
     */
    public String detectObjects(MultipartFile imageFile) {
        // 1. 校验文件是否为空
        if (imageFile.isEmpty()) {
            throw new BusinessException(BusinessEnum.FAILED,"上传的图片文件为空");
        }
        String imageLocalPath = ImageUploadUtils.saveImageToLocal(imageFile);
        return imageLocalPath;
        /*HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 构造请求体（键名 "image" 需与服务端接口一致）
        JSONObject requestBody = new JSONObject();
        requestBody.put("image_path", imageLocalPath);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://127.0.0.1:5000/detect");
        ResponseEntity<JSONObject> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, new HttpEntity<>(requestBody, headers), JSONObject.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(BusinessEnum.FAILED,"model 服务调用失败，状态码：" + response.getStatusCodeValue());
        }
        return response.getBody().toString();*/
    }
}