package com.trionesdev.weixin.web.spring.boot.autoconfigure;

import com.trionesdev.weixin.base.WeiXinCredentials;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "triones.weixin.web")
public class WeiXinWebProperties extends WeiXinCredentials {
    private Boolean enabled;
    private Class<?> cache;
    private Boolean multi;
    private Map<String,WeiXinCredentials> credentials;
}
