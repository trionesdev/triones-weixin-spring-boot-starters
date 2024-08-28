package com.trionesdev.weixin.miniprogram.autoconfigure;


import com.trionesdev.weixin.base.WeiXinCache;
import com.trionesdev.weixin.base.WeiXinConfig;
import com.trionesdev.weixin.base.ex.WeiXinException;
import com.trionesdev.weixin.miniprogram.WeiXinMiniProgram;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.util.Objects;

@RequiredArgsConstructor
@Configuration(value = "com.moensun.weixin.miniprogram.autoconfigure.WeiXinMiniProgramAutoConfiguration")
@ConditionalOnProperty(prefix = "triones.weixin.miniprogram", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(value = {WeiXinMiniProgramProperties.class})
@Import(value = {WeiXinMiniProgramAutoConfiguration.AutoConfiguredRegistrar.class})
public class WeiXinMiniProgramAutoConfiguration {

    public static class AutoConfiguredRegistrar implements EnvironmentAware, BeanFactoryPostProcessor, ApplicationContextAware {

        private WeiXinMiniProgramProperties confProperties;
        private ApplicationContext applicationContext;

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
            configurableListableBeanFactory.addBeanPostProcessor(new WeiXinMiniProgramBeanPostProcessor(confProperties, applicationContext));
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
            WeiXinConfig weiXinConfig = new WeiXinConfig();
            weiXinConfig.setAppId(confProperties.getAppId());
            weiXinConfig.setSecret(confProperties.getSecret());
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

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }

    public static class WeiXinMiniProgramBeanPostProcessor implements BeanPostProcessor {
        private final WeiXinMiniProgramProperties confProperties;
        private final ApplicationContext applicationContext;

        public WeiXinMiniProgramBeanPostProcessor(WeiXinMiniProgramProperties confProperties, ApplicationContext applicationContext) {
            this.confProperties = confProperties;
            this.applicationContext = applicationContext;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof WeiXinMiniProgram) {
                WeiXinCache weiXinCache = null;
                Class<?> cache = confProperties.getCache();
                if (Objects.nonNull(cache)) {
                    if (WeiXinCache.class.isAssignableFrom(cache)) {
                        if (applicationContext.getBeanNamesForType(cache).length > 0) {
                            weiXinCache = (WeiXinCache) applicationContext.getBean(cache);
                        }
                    } else {
                        throw new WeiXinException("cache class is not implements from  `com.moensun.weixin.commons.class`");
                    }
                } else {
                    if (applicationContext.getBeanNamesForType(WeiXinCache.class).length != 0) {
                        weiXinCache = applicationContext.getBean(WeiXinCache.class);
                    }
                }
                ((WeiXinMiniProgram) bean).setWeiXinCache(weiXinCache);
            }
            return bean;
        }


    }

}
