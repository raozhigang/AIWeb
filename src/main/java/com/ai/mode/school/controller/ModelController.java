package com.ai.mode.school.controller;

import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.service.ModelClientService;
import com.ai.mode.school.utils.JsonParser;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@CrossOrigin(
        origins = "http://localhost:9020",  // ✅ 不能用 "*"
        allowCredentials = "true"           // ✅ 允许凭据
)
public class ModelController extends BaseController{

    @Resource
    private ModelClientService modelClientService;
    @Resource
    private JsonParser jsonParser;

    @PostMapping("/put")
    public Result put() {
        // 调用 model 客户端服务
        jsonParser.main();
        return Result.success();

    }
    /**
     * 接收图片并调用 model 检测服务
     * @param imageFile 上传的图片文件
     * @return 检测结果（包含状态码、消息、数据）
     */
    @PostMapping("/uploadImage")
    public Result detect(@RequestParam("image") MultipartFile imageFile,@RequestParam("model") String model) {
            // 调用 model 客户端服务
        JSONObject jsonObject = modelClientService.detectObjects(imageFile,model);
        return Result.success(jsonObject);

    }

    /**
     * 获取1+10的字体模型推理
     *
     * @param imageFile 上传的图片文件
     * @param keyword   图片字体内容
     * @return  推理结果
     */
    @PostMapping("/generateImage")
    public ResponseEntity<byte[]> generateImage(@RequestParam("image") MultipartFile imageFile,@RequestParam("keyword") String keyword,
                                        @RequestParam("model") String model) throws IOException {
        // 调用 model 客户端服务
        String imagePath = modelClientService.generateSimilarImgPath(imageFile, keyword,model);
        // 读取文件内容
        byte[] imageBytes = Files.readAllBytes(new File(imagePath).toPath());

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // 根据实际图片类型调整
        //headers.setContentDispositionFormData("attachment", "imageName");
        headers.setContentDispositionFormData("inline", "imageName");
        headers.setContentLength(imageBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(imageBytes);
    }
}