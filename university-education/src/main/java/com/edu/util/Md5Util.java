package com.edu.util;

import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * MD5 加密工具类
 */
public class Md5Util {
    
    /**
     * 生成随机
     */
    public static String generateSalt() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    /**
     * MD5加密 
     */
    public static String encrypt(String password, String salt) {
        String input = password + salt;
        return DigestUtils.md5DigestAsHex(input.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * 验证密码
     */
    public static boolean verify(String password, String salt, String encryptedPassword) {
        return encrypt(password, salt).equals(encryptedPassword);
    }
    
    /**
     * 简单MD5加密
     */
    public static String md5(String input) {
        return DigestUtils.md5DigestAsHex(input.getBytes(StandardCharsets.UTF_8));
    }
}
