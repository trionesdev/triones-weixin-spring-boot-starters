package com.moensun.weixin.offiaccount.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "triones.weixin.offiaccount")
public class WeiXinOfficeAccountProperties {
    private Boolean enabled;
    private String appId;
    private String secret;
    private Class<?> cache;
}
