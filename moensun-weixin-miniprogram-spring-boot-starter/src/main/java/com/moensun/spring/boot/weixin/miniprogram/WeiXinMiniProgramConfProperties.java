package com.moensun.spring.boot.weixin.miniprogram;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "weixin.miniprogram")
public class WeiXinMiniProgramConfProperties extends WeiXinMiniProgramProperties {
    private Boolean enabled;
    private Map<String, WeiXinMiniProgramInstanceProperties> instance;
}
