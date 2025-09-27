package com.ai.mode.school.controller;

import cn.hutool.json.JSONUtil;
import com.ai.mode.school.beans.dto.BatchFontGenerateReq;
import com.ai.mode.school.beans.dto.FontGenerateButtonReq;
import com.ai.mode.school.beans.dto.FontGenerateDto;
import com.ai.mode.school.beans.dto.WeightDto;
import com.ai.mode.school.beans.entity.FontGeneration;
import com.ai.mode.school.beans.entity.User;
import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.dal.service.FontGenerationServiceImpl;
import com.ai.mode.school.service.ModelClientService;
import com.ai.mode.school.utils.JsonParser;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    private FontGenerationServiceImpl fontGenerationService;

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
    public Result<String> generateImage(@RequestParam("image") MultipartFile imageFile,@RequestParam("keyword") String keyword,
                                        @RequestParam("model") String model) throws IOException {
        // 调用 model 客户端服务
        String imagePath = modelClientService.generateSimilarImgPath(imageFile, keyword,model);
        // 读取文件内容
        byte[] imageBytes = Files.readAllBytes(new File(imagePath).toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        return Result.success(base64Image);
    }


    /**
     * 风格分析按钮
     *
     * @return  推理结果
     */
    //批量操作
    @PostMapping("/batchGenerateImage")
    public Result<BatchFontGenerateReq> generateImage(@RequestBody BatchFontGenerateReq req) throws IOException {
        JSONObject jsonObject = modelClientService.batchGenerateSimilarImgPath(req);
        List<WeightDto> list = new ArrayList<>();
        for (String key : jsonObject.keySet()) {
            WeightDto weightDto = new WeightDto();
            weightDto.setWeightName(key);
            weightDto.setWeightValue(jsonObject.getString(key));
            list.add(weightDto);
        }
        req.setWeightList(list);
        // 返回批量预处理内容
        for (FontGenerateDto dto : req.getData()) {
            String imagePath = dto.getImagePath();
            byte[] imageBytes = Files.readAllBytes(new File(imagePath).toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String base64WithPrefix = "data:image/png;base64," + base64Image;
            dto.setBase64Url(base64WithPrefix);
        }
        //保存操作记录
        User user = getUser();
        FontGeneration fontGeneration =new FontGeneration();
        fontGeneration.setUserName(user.getUsername());
        fontGeneration.setBatchNo(req.getBatchNo());
        fontGeneration.setOperateType("FGFX");
        fontGeneration.setStyleType(req.getModel());
        fontGeneration.setWeights(jsonObject.toString());
        List<String> stringList = req.getData().stream().map(FontGenerateDto::getImagePath).collect(Collectors.toList());
        fontGeneration.setInputImageUrl(JSONUtil.toJsonStr(stringList));
        fontGenerationService.saveFontGeneration(fontGeneration);
        return Result.success(req);
    }


    /**
     * 字体生成按钮
     *
     * @return  生成图像
     */
    @PostMapping("/fontGenerateButton")
    public Result<String> fontGenerateButton(@RequestBody FontGenerateButtonReq req) throws IOException {
        // 调用 model 客户端服务
        User user = getUser();
        String imagePath = modelClientService.fontGenerateButton(req,user.getUsername());
        // 读取文件内容
        byte[] imageBytes = Files.readAllBytes(new File(imagePath).toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String base64WithPrefix = "data:image/png;base64," + base64Image;
        return Result.success(base64WithPrefix);
    }
}