package com.ai.mode.school.controller.business;

import com.ai.mode.school.beans.entity.FontGeneration;
import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.dal.service.FontGenerationServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(
        origins = "http://localhost:9020",  // ✅ 不能用 "*"
        allowCredentials = "true"           // ✅ 允许凭据
)
@RestController
@RequestMapping("/api/fontGeneration")
public class FontGenerationController {
    @Resource
    private FontGenerationServiceImpl service;

    @PostMapping
    public Result<FontGeneration> generate(@RequestBody FontGeneration request) {
        return Result.success();
    }
}
