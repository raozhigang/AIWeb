package com.ai.mode.school.controller.business;

import com.ai.mode.school.beans.dto.UserGenerateReq;
import com.ai.mode.school.beans.entity.FontGeneration;
import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.controller.BaseController;
import com.ai.mode.school.dal.service.FontGenerationServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(
        origins = "http://localhost:9020",  // ✅ 不能用 "*"
        allowCredentials = "true"           // ✅ 允许凭据
)
@RestController
@RequestMapping("/api/fontGeneration")
public class FontGenerationController extends BaseController {
    @Resource
    private FontGenerationServiceImpl service;

    @PostMapping("/list")
    public Result<IPage<FontGeneration>> pageList(@RequestBody UserGenerateReq request) {
        String username = getUser().getUsername();
        request.setUserName(username);
        IPage<FontGeneration> fontGenerationIPage = service.listFontGenerationsByPage(request);
        return Result.success(fontGenerationIPage);
    }

    @PostMapping("/update")
    public Result<FontGeneration> update(@RequestBody FontGeneration request) {
        service.updateFontGeneration(request.getBatchNo(),request.getStyleName());
        return Result.success();
    }
}
