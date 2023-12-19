package com.trionesdev.weixin.miniprogram.autoconfigure;


import com.trionesdev.weixin.base.WeiXinCache;
import com.trionesdev.weixin.base.WeiXinConfig;
import com.trionesdev.weixin.base.ex.WeiXinException;
import com.trionesdev.weixin.miniprogram.WeiXinMiniProgram;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.util.Objects;

@RequiredArgsConstructor
@Configuration(value = "com.moensun.weixin.miniprogram.autoconfigure.WeiXinMiniProgramAutoConfiguration")
@ConditionalOnProperty(prefix = "triones.weixin.miniprogram", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(value = {WeiXinMiniProgramProperties.class})
public class WeiXinMiniProgramAutoConfiguration {

    private final WeiXinMiniProgramProperties confProperties;


    @Configuration
    @Import(value = {AutoConfiguredRegistrar.class})
    public static class AutoConfiguredRegistrarConfiguration implements InitializingBean {
        @Override
        public void afterPropertiesSet() throws Exception {

        }
    }

    public static class AutoConfiguredRegistrar implements EnvironmentAware, BeanFactoryPostProcessor {

        private WeiXinMiniProgramProperties confProperties;

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
            WeiXinCache weiXinCache = null;
            Class<?> cache = confProperties.getCache();
            if (Objects.nonNull(cache)) {
                if (WeiXinCache.class.isAssignableFrom(cache)) {
                    if (beanFactory.getBeanNamesForType(cache).length > 0) {
                        weiXinCache = (WeiXinCache) beanFactory.getBean(cache);
                    }
                } else {
                    throw new WeiXinException("cache class is not implements from  `com.moensun.weixin.commons.class`");
                }
            } else {
                if (beanFactory.getBeanNamesForType(WeiXinCache.class).length != 0) {
                    weiXinCache = beanFactory.getBean(WeiXinCache.class);
                }
            }

            WeiXinConfig weiXinConfig = new WeiXinConfig();
            weiXinConfig.setAppId(confProperties.getAppId());
            weiXinConfig.setSecret(confProperties.getSecret());
            weiXinConfig.setWeiXinCache(weiXinCache);
            ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
            argumentValues.addIndexedArgumentValue(0, weiXinConfig);
            registerBean(beanFactory, argumentValues, WeiXinMiniProgram.class.getName());
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.confProperties = Binder.get(environment).bind("triones.weixin.miniprogram", WeiXinMiniProgramProperties.class).get();
        }

        private void registerBean(DefaultListableBeanFactory beanFactory, ConstructorArgumentValues argumentValues, String beanName) {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(WeiXinMiniProgram.class);
            beanDefinition.setBeanClassName(WeiXinMiniProgram.class.getName());
            beanDefinition.setConstructorArgumentValues(argumentValues);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
    }

}
