package com.macro.mall.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashSet;

/**
 * 地址过滤器
 * @Author superUser
 * @Date 2021/6/13
 */
@ConfigurationProperties(prefix = "member.auth")
@Data
public class NoAuthUrlProperties {
    private LinkedHashSet<String> shouldSkipUrls;
}
