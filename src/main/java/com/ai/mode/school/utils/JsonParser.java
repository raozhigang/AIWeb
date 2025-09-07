package com.ai.mode.school.utils;

import com.ai.mode.school.beans.entity.FontData;
import com.ai.mode.school.dal.service.FontDataServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JsonParser {

    @Resource
    private FontDataServiceImpl fontDataService;
    public void main() {
        // JSON文件路径（需要将提供的JSON数据保存到该文件）C:\ABW-Font
        String jsonFilePath = "C:\\ABW-Font\\char_to_images.json";
        
        try {
            // 读取JSON文件并解析到Map
            Map<String, List<String>> map = parseJson(jsonFilePath);
            
            // 打印前5个条目验证
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                List<FontData> fontDataList =new ArrayList<>();
                String key = entry.getKey();
                for(String url : entry.getValue()){
                    FontData fontData =new FontData();
                    fontData.setFontValue(key);
                    fontData.setImageAbsUrl(url);
                    fontDataList.add(fontData);
                }
                fontDataService.batchInsert(fontDataList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<String>> parseJson(String filePath) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<String>>>(){}.getType();
        
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, type);
        }
    }
}