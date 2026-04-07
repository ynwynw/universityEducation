package com.edu.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 配置类
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 登录校验 -- 排除登录相关接口
            SaRouter.match("/**")
                    .notMatch("/auth/login", "/auth/captcha", "/auth/register", "/auth/forgot-password")
                    .notMatch("/file/preview/**", "/file/download/**")
                    .notMatch("/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**")
                    .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }
}
