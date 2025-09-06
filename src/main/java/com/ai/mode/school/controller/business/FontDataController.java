package com.ai.mode.school.controller.business;

import com.ai.mode.school.beans.entity.FontData;
import com.ai.mode.school.common.response.Result;
import com.ai.mode.school.dal.service.FontDataServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin(
        origins = "http://localhost:9020",  // ✅ 不能用 "*"
        allowCredentials = "true"           // ✅ 允许凭据
)
@RestController
@RequestMapping("/api/fontData")
public class FontDataController {
    @Resource
    private FontDataServiceImpl service;

    @GetMapping("/search")
    public Result<List<FontData>> search(@RequestParam String fontValue, @RequestParam String styleType) {
        return Result.success();
    }
}
