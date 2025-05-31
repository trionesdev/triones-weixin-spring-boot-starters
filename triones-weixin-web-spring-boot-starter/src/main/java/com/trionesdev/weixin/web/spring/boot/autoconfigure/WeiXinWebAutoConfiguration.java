package com.trionesdev.weixin.web.spring.boot.autoconfigure;

import com.trionesdev.weixin.base.WeiXinCache;
import com.trionesdev.weixin.base.WeiXinConfig;
import com.trionesdev.weixin.base.ex.WeiXinException;
import com.trionesdev.weixin.web.WeiXinWeb;
import org.jetbrains.annotations.NotNull;
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

@Configuration(value = "com.trionesdev.weixin.web.autoconfigure.WeiXinWebAutoConfiguration")
@ConditionalOnProperty(prefix = "triones.weixin.web", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(value = {WeiXinWebProperties.class})
@Import(value = {WeiXinWebAutoConfiguration.AutoConfiguredRegistrar.class})
public class WeiXinWebAutoConfiguration {

    public static class AutoConfiguredRegistrar implements EnvironmentAware, BeanFactoryPostProcessor, ApplicationContextAware {
        private WeiXinWebProperties confProperties;
        private ApplicationContext applicationContext;

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
            configurableListableBeanFactory.addBeanPostProcessor(new WeiXinWebBeanPostProcessor(confProperties, applicationContext));
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
            WeiXinConfig weiXinConfig = new WeiXinConfig();
            weiXinConfig.setAppId(confProperties.getAppId());
            weiXinConfig.setSecret(confProperties.getSecret());
            weiXinConfig.setMulti(confProperties.getMulti());
            weiXinConfig.setCredentials(confProperties.getCredentials());
            ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
            argumentValues.addIndexedArgumentValue(0, weiXinConfig);
            registerBean(beanFactory, argumentValues, WeiXinWeb.class.getName());
        }

        @Override
        public void setEnvironment(@NotNull Environment environment) {
            this.confProperties = Binder.get(environment).bind("triones.weixin.web", WeiXinWebProperties.class).get();
        }

        private void registerBean(DefaultListableBeanFactory beanFactory, ConstructorArgumentValues argumentValues, String beanName) {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(WeiXinWeb.class);
            beanDefinition.setBeanClassName(WeiXinWeb.class.getName());
            beanDefinition.setConstructorArgumentValues(argumentValues);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        }

        @Override
        public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }

    public static class WeiXinWebBeanPostProcessor implements BeanPostProcessor {
        private final WeiXinWebProperties confProperties;
        private final ApplicationContext applicationContext;

        public WeiXinWebBeanPostProcessor(WeiXinWebProperties confProperties, ApplicationContext applicationContext) {
            this.confProperties = confProperties;
            this.applicationContext = applicationContext;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof WeiXinWeb) {
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
                ((WeiXinWeb) bean).setWeiXinCache(weiXinCache);
            }
            return bean;
        }

    }


}
