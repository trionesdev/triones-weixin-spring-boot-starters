package com.moensun.weixin.miniprogram.annotation;

import com.moensun.weixin.commons.WeiXinCache;
import com.moensun.weixin.commons.WeiXinConfig;
import com.moensun.weixin.commons.ex.WeiXinException;
import com.moensun.weixin.miniprogram.MiniProgram;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class WeiXinMiniProgramClientFactoryBean implements FactoryBean<Object>, InitializingBean,
        ApplicationContextAware, BeanFactoryAware {
    private String appId;
    private String secret;
    private Class<?> cache;

    private Class<?> type;
    private BeanFactory beanFactory;

    private ApplicationContext applicationContext;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getObject() {
        return getTarget();
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setCache(Class<?> cache) {
        this.cache = cache;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    protected <T> T getTarget() {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        WeiXinCache weiXinCache = null;
        if (Objects.nonNull(cache)) {
            if (WeiXinCache.class.isAssignableFrom(cache)) {
                if (listableBeanFactory.getBeanNamesForType(cache).length>0){
                    weiXinCache = (WeiXinCache) beanFactory.getBean(cache);
                }
            } else {
                throw new WeiXinException("cache class is not implements from  `com.moensun.weixin.commons.class`");
            }
        } else {
            if (listableBeanFactory.getBeanNamesForType(WeiXinCache.class).length>0){
                weiXinCache = beanFactory.getBean(WeiXinCache.class);
            }
        }
        WeiXinConfig weiXinConfig = new WeiXinConfig();
        weiXinConfig.setAppId(appId);
        weiXinConfig.setSecret(secret);
        weiXinConfig.setWeiXinCache(weiXinCache);
        MiniProgram miniProgram = new MiniProgram(weiXinConfig);
        return (T) this.type.cast(Proxy.newProxyInstance(this.type.getClassLoader(), new Class[]{this.type}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(miniProgram, args);
            }
        }));
    }
}
