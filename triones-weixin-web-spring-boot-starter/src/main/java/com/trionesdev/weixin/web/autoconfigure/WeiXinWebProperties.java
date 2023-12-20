package com.trionesdev.weixin.web.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "triones.weixin.web")
public class WeiXinWebProperties {
    private Boolean enabled;
    private String appId;
    private String secret;
    private Class<?> cache;
}
