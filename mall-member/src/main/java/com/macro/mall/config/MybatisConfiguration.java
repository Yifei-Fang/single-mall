package com.macro.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author superUser
 * @Date 2021/6/10
 */
@Configuration
@EnableTransactionManagement    //开启事务
@MapperScan(basePackages = {"com.macro.mall.mapper","com.macro.mall.dao"})
public class MybatisConfiguration {
}
