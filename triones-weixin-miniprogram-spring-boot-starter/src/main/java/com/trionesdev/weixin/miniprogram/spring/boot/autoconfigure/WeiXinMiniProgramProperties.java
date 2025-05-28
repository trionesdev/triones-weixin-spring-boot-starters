package com.trionesdev.weixin.miniprogram.spring.boot.autoconfigure;

import com.trionesdev.weixin.base.WeiXinIdentity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "triones.weixin.miniprogram")
public class WeiXinMiniProgramProperties extends WeiXinIdentity {
    private Boolean enabled;
    private Class<?> cache;
    private List<WeiXinIdentity> multi;
}
