package com.ai.mode.school.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class ImageUploadUtils {

    private static final Logger log = LoggerFactory.getLogger(ImageUploadUtils.class);

    // 注入配置的本地存储基础路径
    private final static String basePath = "D:\\data_kitti\\uploadImg";

    // 是否覆盖已存在的文件（默认不覆盖）
    private final static boolean overwrite = false;

    // 简单时间格式（年月日时分秒，14位数字）
    private static final String TIME_FORMAT = "yyyyMMddHHmmss";
    // 随机数长度（可选，若需简单去重可添加 2-3 位）
    private static final int RANDOM_LENGTH = 2;
    private static final Random random = new Random();

    public static MultipartFile base64ToMultipart(String base64, String fileName) {
        try {
            String[] parts = base64.split(",");

            String base64Data;
            String contentType = "application/octet-stream";

            if (parts.length == 2) {
                // 带 data:image/png;base64 前缀
                base64Data = parts[1];
                String dataPrefix = parts[0];
                if (dataPrefix.contains(":") && dataPrefix.contains(";")) {
                    contentType = dataPrefix.substring(dataPrefix.indexOf(":") + 1, dataPrefix.indexOf(";"));
                }
            } else {
                // 纯 Base64
                base64Data = base64;
            }

            byte[] fileContent = Base64.getDecoder().decode(base64Data);

            return new Base64MultipartFile(
                    fileContent,
                    fileName,
                    fileName,
                    contentType
            );

        } catch (Exception e) {
            throw new RuntimeException("Base64 转 MultipartFile 失败", e);
        }
    }
    /**
     * 保存上传的图片到本地指定路径
     * @param file 上传的 MultipartFile 文件
     * @return 保存后的文件绝对路径（失败返回 null）
     */
    public static String saveImageToLocal(MultipartFile file) {
        if (file.isEmpty()) {
            log.warn("文件为空，保存失败");
            return null;
        }
        try {
            // 1. 生成唯一文件名（时间戳 + 原扩展名）
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = generateUniqueFilename(fileExtension);
            // 2. 构造完整保存路径（basePath + uniqueFilename）
            File targetDir = new File(basePath);
            if (!targetDir.exists() && !targetDir.mkdirs()) {
                log.error("目标目录创建失败：{}", basePath);
                return null;
            }
            File targetFile = new File(targetDir, uniqueFilename);
            // 3. 检查文件是否已存在（根据配置决定是否覆盖）
            if (targetFile.exists() && !overwrite) {
                log.warn("文件已存在且禁止覆盖，跳过保存：{}", targetFile.getAbsolutePath());
                return null;
            }
            // 4. 保存文件到本地
            file.transferTo(targetFile);
            log.info("文件保存成功，路径：{}", targetFile.getAbsolutePath());
            return targetFile.getAbsolutePath();

        } catch (IOException e) {
            log.error("文件保存失败，原因：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取文件扩展名（如 "jpg"、"png"）
     * @param originalFilename 原文件名（如 "test.jpg"）
     * @return 扩展名（不带点，如 "jpg"；无扩展名返回 null）
     */
    private static String getFileExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return null;
        }
        int lastDotIndex = originalFilename.lastIndexOf(".");
        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * 生成唯一文件名（时间戳+可选随机数+扩展名）
     * @param fileExtension 文件扩展名（如 "jpg"）
     * @return 唯一文件名（如 "20250815173023-12.jpg" 或 "20250815173023.jpg"）
     */
    private static String generateUniqueFilename(String fileExtension) {
        String timestamp = new SimpleDateFormat(TIME_FORMAT).format(new Date());
        // 可选：添加 2 位随机数（降低并发冲突概率）
        String randomPart = RANDOM_LENGTH > 0 ? "-" + String.format("%02d", random.nextInt(100)) : "";
        // 组合结果（时间戳+随机数.扩展名）
        return String.format("%s%s.%s", timestamp, randomPart, fileExtension);
    }
}