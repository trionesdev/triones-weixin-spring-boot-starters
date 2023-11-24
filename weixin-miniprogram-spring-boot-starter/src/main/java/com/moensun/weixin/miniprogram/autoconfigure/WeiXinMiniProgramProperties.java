package com.moensun.weixin.miniprogram.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "weixin.miniprogram")
public class WeiXinMiniProgramProperties {
    private Boolean enabled;
    private String appId;
    private String secret;
    private Class<?> cache;
}
