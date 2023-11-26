package com.moensun.weixin.offiaccount.annotation;

import com.moensun.weixin.commons.WeiXinCache;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WeiXinOfficeAccountClient {
    String appId() default "";

    String secret() default "";

    Class<WeiXinCache> cache() default WeiXinCache.class;
}
