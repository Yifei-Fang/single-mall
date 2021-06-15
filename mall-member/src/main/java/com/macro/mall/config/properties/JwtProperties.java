package com.macro.mall.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author superUser
 * @Date 2021/6/14
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private long expiration;

    private String secret;

    private String tokenHeader;

    private String tokenHead;

}
