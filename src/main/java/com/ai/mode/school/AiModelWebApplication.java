package com.ai.mode.school;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ai.mode.school.dal.mapper")
public class AiModelWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiModelWebApplication.class, args);
    }

}
