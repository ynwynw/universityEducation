package com.edu.controller;

import com.edu.annotation.Log;
import com.edu.common.Result;
import com.edu.entity.SysMenu;
import com.edu.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public Result<Map<String, String>> getCaptcha() {
        return Result.success(authService.generateCaptcha());
    }
    
    /**
     * 登录
     */
    @PostMapping("/login")
    @Log(module = "认证", operation = "用户登录")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginForm) {
        String username = loginForm.get("username");
        String password = loginForm.get("password");
        String captcha = loginForm.get("captcha");
        String captchaKey = loginForm.get("captchaKey");
        return Result.success(authService.login(username, password, captcha, captchaKey));
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo() {
        return Result.success(authService.getUserInfo());
    }
    
    /**
     * 获取用户菜单
     */
    @GetMapping("/menus")
    public Result<List<SysMenu>> getMenus() {
        return Result.success(authService.getUserMenus());
    }
    
    /**
     * 登出
     */
    @PostMapping("/logout")
    @Log(module = "认证", operation = "用户登出")
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    @Log(module = "认证", operation = "修改密码")
    public Result<Void> changePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        authService.changePassword(oldPassword, newPassword);
        return Result.success();
    }
}
