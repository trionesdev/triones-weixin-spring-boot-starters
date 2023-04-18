package com.moensun.weixin.spring.boot.miniprogram;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WeiXinMiniProgramInstanceProperties extends WeiXinMiniProgramProperties {
    private String name;
}
