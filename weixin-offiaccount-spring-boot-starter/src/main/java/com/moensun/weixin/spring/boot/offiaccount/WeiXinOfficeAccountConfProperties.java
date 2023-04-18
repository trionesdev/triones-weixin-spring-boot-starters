package com.moensun.weixin.spring.boot.offiaccount;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "weixin.offiaccount")
public class WeiXinOfficeAccountConfProperties extends WeiXinOfficeAccountProperties {
    private Boolean enabled;
    private Map<String, WeiXinOfficeAccountInstanceProperties> instance;
}
