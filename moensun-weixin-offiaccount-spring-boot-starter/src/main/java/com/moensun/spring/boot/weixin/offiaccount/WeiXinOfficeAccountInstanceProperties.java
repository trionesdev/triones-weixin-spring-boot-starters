package com.moensun.spring.boot.weixin.offiaccount;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WeiXinOfficeAccountInstanceProperties extends WeiXinOfficeAccountProperties{
    private String name;
}
