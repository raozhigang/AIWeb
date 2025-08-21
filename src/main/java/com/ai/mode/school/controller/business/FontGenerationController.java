package com.ai.mode.school.controller.business;

import com.ai.mode.school.beans.entity.FontGeneration;
import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.dal.service.FontGenerationServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

// FontGenerationController.java
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
