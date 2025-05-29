package com.trionesdev.weixin.miniprogram.spring.boot.autoconfigure;

import com.trionesdev.weixin.base.WeiXinCredentials;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "triones.weixin.miniprogram")
public class WeiXinMiniProgramProperties extends WeiXinCredentials {
    private Boolean enabled;
    private Class<?> cache;
    private Boolean multi;
    private List<WeiXinCredentials> credentials;
}
