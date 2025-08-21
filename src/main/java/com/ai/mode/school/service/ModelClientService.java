package com.ai.mode.school.service;

import cn.hutool.json.JSONUtil;
import com.ai.mode.school.beans.entity.User;
import com.ai.mode.school.common.exception.BusinessException;
import com.ai.mode.school.dal.service.FontGenerationServiceImpl;
import com.ai.mode.school.utils.ImageUploadUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;

@Slf4j
@Service
public class ModelClientService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private FontGenerationServiceImpl fontGenerationService;

    private final String modelServiceUrl = "http://127.0.0.1:5000";


    /**
     * 调用 model 推理服务，上传图片并获取检测结果
     * @param imageFile 上传的图片文件（MultipartFile）
     */
    public JSONObject detectObjects(MultipartFile imageFile, User user) {
        // 1. 校验文件是否为空
        if (imageFile.isEmpty()) {
            throw new BusinessException("上传的图片文件为空");
        }
        String imageLocalPath = ImageUploadUtils.saveImageToLocal(imageFile);
        if(imageLocalPath == null){
            throw new BusinessException("图片保存本地失败");
        }
        fontGenerationService.saveFontGeneration(user.getUsername(),imageLocalPath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 构造请求体（键名 "image" 需与服务端接口一致）
        JSONObject requestBody = new JSONObject();
        requestBody.put("image_path", imageLocalPath);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(modelServiceUrl+"/detect");
        log.info("model服务请求信息:{}",JSONUtil.toJsonStr(requestBody));
        ResponseEntity<JSONObject> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, new HttpEntity<>(requestBody, headers), JSONObject.class);
        log.info("model服务返回信息:{}",JSONUtil.toJsonStr(response));
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody()==null) {
            throw new BusinessException("model 服务调用失败!");
        }
        return response.getBody().getJSONObject("detections");
    }


    /**
     * 获取1+10的字体模型推理
     * @param imageFile
     * @param keyword
     * @return  返回图片地址
     */
    public String generateSimilarImgPath(MultipartFile imageFile,String keyword) {
        if (imageFile.isEmpty() || StringUtils.isEmpty(keyword)) {
            throw new BusinessException("请求参数不全");
        }
        String imageLocalPath = ImageUploadUtils.saveImageToLocal(imageFile);
        if(imageLocalPath == null){
            throw new BusinessException("图片保存本地失败");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("image_path", imageLocalPath);
        requestBody.put("keyword",keyword);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(modelServiceUrl+"/generateImg");
        log.info("model服务请求信息:{}",JSONUtil.toJsonStr(requestBody));
        ResponseEntity<JSONObject> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, new HttpEntity<>(requestBody, headers), JSONObject.class);
        log.info("model服务返回信息:{}",JSONUtil.toJsonStr(response));
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody()==null) {
            throw new BusinessException("model 服务调用失败，状态码：" + response.getStatusCodeValue());
        }
        return response.getBody().getString("imgPath");
    }
}