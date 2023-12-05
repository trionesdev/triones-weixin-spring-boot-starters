package com.trionesdev.weixin.offiaccount.annotation;

import com.trionesdev.weixin.commons.WeiXinCache;

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
