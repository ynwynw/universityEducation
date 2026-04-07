package com.edu.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 配置类
 */
@Configuration
@MapperScan("com.edu.mapper")
public class MyBatisConfig {
    // MyBatis 配置由 application.yml 管理
    // PageHelper 分页插件自动配置
}
