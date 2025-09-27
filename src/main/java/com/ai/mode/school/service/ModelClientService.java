package com.ai.mode.school.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.json.JSONUtil;
import com.ai.mode.school.beans.dto.BatchFontGenerateReq;
import com.ai.mode.school.beans.dto.FontGenerateButtonReq;
import com.ai.mode.school.beans.dto.FontGenerateDto;
import com.ai.mode.school.beans.dto.FontGenerateModelReq;
import com.ai.mode.school.beans.entity.FontData;
import com.ai.mode.school.beans.entity.FontGeneration;
import com.ai.mode.school.beans.entity.User;
import com.ai.mode.school.common.exception.BusinessException;
import com.ai.mode.school.dal.service.FontDataServiceImpl;
import com.ai.mode.school.dal.service.FontGenerationServiceImpl;
import com.ai.mode.school.utils.ImageUploadUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ModelClientService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private FontGenerationServiceImpl fontGenerationService;

    @Resource
    private FontDataServiceImpl fontDataService;

    private final String modelServiceUrl = "http://127.0.0.1:5000";

    private final Snowflake snowflake = new Snowflake();

    /**
     * 调用 model 推理服务，上传图片并获取检测结果
     * @param imageFile 上传的图片文件（MultipartFile）
     */
    public JSONObject detectObjects(MultipartFile imageFile, String model) {
        // 1. 校验文件是否为空
        if (imageFile.isEmpty()) {
            throw new BusinessException("上传的图片文件为空");
        }
        String imageLocalPath = ImageUploadUtils.saveImageToLocal(imageFile);
        if(imageLocalPath == null){
            throw new BusinessException("图片保存本地失败");
        }
        //保存用户操作记录信息
        //fontGenerationService.saveFontGeneration(user.getUsername(),imageLocalPath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("image_path", imageLocalPath);
        requestBody.put("model",model);
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
    public String generateSimilarImgPath(MultipartFile imageFile,String keyword,String model) {
        if (imageFile.isEmpty() || StringUtils.isEmpty(keyword)) {
            throw new BusinessException("请求参数不全");
        }
        String imageLocalPath = ImageUploadUtils.saveImageToLocal(imageFile);
        if(imageLocalPath == null){
            throw new BusinessException("图片保存本地失败");
        }
        List<FontData> fontData = fontDataService.listFontsByValue(keyword, model);
        if(CollectionUtil.isEmpty(fontData)){
            throw new BusinessException("暂不支持该文字，请联系管理员维护");
        }
        List<String> basis_path = fontData.stream().map(FontData::getImageAbsUrl).collect(Collectors.toList());
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject requestBody = new JSONObject();
            requestBody.put("image_path", imageLocalPath);
            requestBody.put("keyword",keyword);
            requestBody.put("model",model);
            requestBody.put("basis_path",basis_path);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(modelServiceUrl+"/generateImg");
            log.info("model服务请求信息:{}",JSONUtil.toJsonStr(requestBody));
            ResponseEntity<JSONObject> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, new HttpEntity<>(requestBody, headers), JSONObject.class);
            log.info("model服务返回信息:{}",JSONUtil.toJsonStr(response));
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody()==null) {
                throw new BusinessException("model 服务调用失败，状态码：" + response.getStatusCodeValue());
            }
            return response.getBody().getString("generate_img_path");
        } catch (Exception e) {
            log.error("模型服务异常:{}",e.getMessage());
        }
        return imageLocalPath;
    }

    public JSONObject batchGenerateSimilarImgPath(BatchFontGenerateReq req) {
        if (CollectionUtil.isEmpty(req.getData())) {
            throw new BusinessException("请求参数不全");
        }
        String batchNo = snowflake.nextIdStr();
        req.setBatchNo(batchNo);
        log.info("批量操作集合:{}",JSONUtil.toJsonStr(req));

        //OCR识别字体
        this.OCRAndSaveKeyword(req);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject requestBody = new JSONObject();
            requestBody.put("req", req);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(modelServiceUrl+"/batchGenerateImg");
            log.info("model服务权重计算请求信息:{}",JSONUtil.toJsonStr(requestBody));
            ResponseEntity<JSONObject> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, new HttpEntity<>(requestBody, headers), JSONObject.class);
            log.info("model服务权重计算返回信息:{}",JSONUtil.toJsonStr(response));
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody()==null) {
                throw new BusinessException("model服务权重计算调用失败，状态码：" + response.getStatusCodeValue());
            }
            JSONObject body = response.getBody();
            return body.getJSONObject("received_items");
        } catch (Exception e) {
            log.error("模型服务权重计算异常:{}",e.getMessage());
        }
        return null;
    }

    public void OCRAndSaveKeyword(BatchFontGenerateReq req) {
        int count = 1;
        //base64转换为地址
        for (FontGenerateDto dto : req.getData()) {
            MultipartFile multipartFile = ImageUploadUtils.base64ToMultipart(dto.getUrl(), req.getBatchNo() + count + ".png");
            String imageToLocal = ImageUploadUtils.saveImageToLocal(multipartFile);
            dto.setImagePath(imageToLocal);
            dto.setUrl(null);
            count++;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("req", req);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(modelServiceUrl+"/batchOCRImg");
        log.info("model服务OCR识别请求信息:{}",JSONUtil.toJsonStr(requestBody));
        ResponseEntity<JSONObject> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, new HttpEntity<>(requestBody, headers), JSONObject.class);
        log.info("model服务OCR识别返回信息:{}",JSONUtil.toJsonStr(response));
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody()==null) {
            throw new BusinessException("model服务OCR识别调用失败，状态码：" + response.getStatusCodeValue());
        }
        JSONObject object = response.getBody();
        JSONArray receivedItems = object.getJSONArray("received_items");
        req.getData().clear();
        for (int i = 0; i < receivedItems.size(); i++) {
            JSONObject jsonObject = receivedItems.getJSONObject(i);
            String imageFile = jsonObject.getString("imageFile");
            String keyword = "";
            if(CollectionUtil.isNotEmpty(jsonObject.getJSONArray("texts"))) {
                keyword = jsonObject.getJSONArray("texts").getString(0);
            }
            FontGenerateDto fontGenerateDto = new FontGenerateDto();
            fontGenerateDto.setImagePath(imageFile);
            fontGenerateDto.setKeyword(keyword);
            req.getData().add(fontGenerateDto);
        }
        //根据识别出的keyword查库
        for (FontGenerateDto fontGenerateDto : req.getData()) {
            List<FontData> fontData = fontDataService.listFontsByValue(fontGenerateDto.getKeyword(), req.getModel());
            if(CollectionUtil.isEmpty(fontData)){
                log.warn("字体数据库查询异常,未查询到该关键字:{}",fontGenerateDto.getKeyword());
                continue;
            }
            List<String> basis_path = fontData.stream().map(FontData::getImageAbsUrl).collect(Collectors.toList());
            fontGenerateDto.setBasisPath(basis_path);
        }
    }

    /**
     * 字体生成按钮
     *
     * @param req 目标文字
     */
    public String fontGenerateButton(FontGenerateButtonReq req,String userName) {
        // 1. 校验数据是否为空
        if (StringUtils.isEmpty(req.getStyleName()) || CollectionUtil.isEmpty(req.getTargetWords())) {
            throw new BusinessException("参数缺失");
        }
        //保存用户操作记录信息
        //fontGenerationService.saveFontGeneration(user.getUsername(),imageLocalPath);

        FontGeneration byStyleName = fontGenerationService.getByStyleName(userName, req.getStyleName());
        if(byStyleName == null){
            throw new BusinessException("该风格不存在");
        }
        FontGenerateModelReq modelReq =new FontGenerateModelReq();
        List<FontGenerateDto> targetWords =new ArrayList<>();
        modelReq.setModel(byStyleName.getStyleType());
        modelReq.setReferenceImage(byStyleName.getInputImageUrl());
        modelReq.setWeights(byStyleName.getWeights());
        modelReq.setTargetWords(targetWords);

        for (String targetWord : req.getTargetWords()) {
            FontGenerateDto fontGenerateDto =new FontGenerateDto();
            List<FontData> fontData = fontDataService.listFontsByValue(targetWord, byStyleName.getStyleType());
            if(CollectionUtil.isEmpty(fontData)){
                log.warn("字体数据库查询异常,未查询到该关键字:{}",targetWord);
                continue;
            }
            List<String> basis_path = fontData.stream().map(FontData::getImageAbsUrl).collect(Collectors.toList());
            fontGenerateDto.setBasisPath(basis_path);
            fontGenerateDto.setKeyword(targetWord);
            targetWords.add(fontGenerateDto);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        requestBody.put("req", modelReq);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(modelServiceUrl+"/fontGenerateButton");
        log.info("model字体生成按钮服务请求信息:{}",JSONUtil.toJsonStr(requestBody));
        ResponseEntity<JSONObject> response = restTemplate.exchange(builder.build().toUri(), HttpMethod.POST, new HttpEntity<>(requestBody, headers), JSONObject.class);
        log.info("model字体生成按钮服务返回信息:{}",JSONUtil.toJsonStr(response));
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody()==null) {
            throw new BusinessException("model 服务调用失败!");
        }
        return response.getBody().getString("detections");
    }
}