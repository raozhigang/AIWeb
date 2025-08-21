package com.ai.mode.school.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

/**
 * JWT 工具类（HS512 算法）
 */
@Component
public class JwtUtils {

    /**
     * 示例密钥（64 字节 Base64 编码，仅用于测试！生产环境需替换）
     * 生成方式：head -c 64 /dev/urandom | base64 | tr -d '='（注意：实际应保留填充符）
     */
    private static final String TEST_SECRET_KEY_BASE64 = "7H5j8kPAAA9s0qR1tW2yU37H5j8kPAAA9s0qR1tWiAo4s6dF7gH8jK9lM0nP1oI2uY3aR4tE5wQ6sD7fG8hJ9k=="; // 注意末尾的 ==

    /**
     * Token 过期时间（12 小时，单位：毫秒）
     */
    private static final long EXPIRE_TIME_MS = 12 * 60 * 60 * 1000;

    /**
     * 生成 JWT Token（HS512 算法）
     * @param userId 用户 ID（Token 主题）
     * @return JWT Token 字符串
     */
    public static String generateToken(String userId) {
        Objects.requireNonNull(userId, "用户 ID 不能为空");
        SecretKey secretKey = getSecretKey(TEST_SECRET_KEY_BASE64);
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME_MS))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 解析 JWT Token 获取用户 ID
     * @param token JWT Token 字符串
     * @return 用户 ID（无效 Token 返回 null）
     */
    public static String parseToken(String token) {
        try {
            SecretKey secretKey = getSecretKey(TEST_SECRET_KEY_BASE64);
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            System.err.println("解析 Token 失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 验证 JWT Token 有效性
     * @param token JWT Token 字符串
     * @return 有效返回 true，无效返回 false
     */
    public static boolean validateToken(String token) {
        return parseToken(token) != null;
    }

    /**
     * 从 Base64 字符串加载 HS512 密钥（验证长度和格式）
     * @param secretKeyBase64 Base64 编码的密钥
     * @return SecretKey 对象
     * @throws IllegalArgumentException 密钥不符合要求时抛出
     */
    private static SecretKey getSecretKey(String secretKeyBase64) {
        // 校验 Base64 字符串长度（64 字节的 Base64 编码长度固定为 88）
        if (secretKeyBase64.length() != 88) {
            throw new IllegalArgumentException("HS512 密钥 Base64 长度必须为 88，当前长度：" + secretKeyBase64.length());
        }

        // 解码 Base64 密钥
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(secretKeyBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Base64 解码失败，无效字符或格式: " + e.getMessage());
        }

        // 校验密钥长度（HS512 要求 64 字节）
        if (keyBytes.length != 64) {
            throw new IllegalArgumentException("HS512 密钥必须为 64 字节，当前长度：" + keyBytes.length);
        }

        return new SecretKeySpec(keyBytes, "HmacSHA512");
    }

    // 测试入口
    public static void main(String[] args) {
        // 生成测试 Token（使用正确长度的密钥）
        String token = generateToken("123456");
        System.out.println("生成的 Token: " + token);

        // 解析 Token
        String userId = parseToken(token);
        System.out.println("解析的用户 ID: " + userId);  // 应输出 "123456"

        // 验证 Token 有效性
        boolean isValid = validateToken(token);
        System.out.println("Token 有效性: " + isValid);  // 应输出 "true"
    }
}