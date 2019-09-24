package com.zhouzifei.blog.framework.config;

import org.springframework.stereotype.Component;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 
 * @author Dabao (17611555590@163.com)
 * @version 1.0
 * @website https://www.zhouzifei.com
 * @date 2019年7月16日
 * @since 1.0
 */
@Component
@MapperScan("com.zhouzifei.blog.persistence.mapper")
public class MybatisConfig {
}
