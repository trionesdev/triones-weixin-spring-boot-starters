package com.moensun.weixin.offiaccount.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(value = {WeiXinOfficeAccountClientRegister.class})
public @interface EnableWeiXinOfficeAccountClients {
    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    Class<?>[] clients() default {};
}
