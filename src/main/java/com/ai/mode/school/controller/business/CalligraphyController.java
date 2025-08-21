package com.ai.mode.school.controller.business;

import com.ai.mode.school.beans.entity.Calligraphy;
import com.ai.mode.school.dal.service.CalligraphyServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.ai.mode.school.common.response.Result;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

// CalligraphyController.java
@RestController
@RequestMapping("/api/calligraphy")
public class CalligraphyController {
    @Resource
    private CalligraphyServiceImpl service;

    @PostMapping
    public Result<Calligraphy> create(@RequestBody Calligraphy calligraphy) {
        return Result.success();
    }

    @GetMapping("/user/{userId}")
    public Result<List<Calligraphy>> getUserWorks(@PathVariable Long userId) {
        return Result.success();
    }
}
