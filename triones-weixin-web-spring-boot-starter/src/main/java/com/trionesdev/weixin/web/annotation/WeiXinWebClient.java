package com.trionesdev.weixin.web.annotation;


import com.trionesdev.weixin.base.WeiXinCache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WeiXinWebClient {
    String appId() default "";

    String secret() default "";

    Class<WeiXinCache> cache() default WeiXinCache.class;
}
