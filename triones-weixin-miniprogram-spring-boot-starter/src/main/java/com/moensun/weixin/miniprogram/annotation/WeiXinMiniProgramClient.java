package com.moensun.weixin.miniprogram.annotation;

import com.moensun.weixin.commons.WeiXinCache;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WeiXinMiniProgramClient {
    String appId() default "";

    String secret() default "";

    Class<WeiXinCache> cache() default WeiXinCache.class;
}
