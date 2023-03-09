package com.moensun.spring.boot.weixin.offiaccount;

import com.moensun.weixin.commons.WeiXinCache;
import lombok.Data;

@Data
public class WeiXinOfficeAccountProperties {
    private String appId;
    private String secret;
}
