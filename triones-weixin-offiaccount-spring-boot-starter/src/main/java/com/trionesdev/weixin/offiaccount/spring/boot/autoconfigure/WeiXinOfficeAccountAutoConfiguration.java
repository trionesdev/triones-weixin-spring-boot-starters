package com.trionesdev.weixin.offiaccount.spring.boot.autoconfigure;

import com.trionesdev.weixin.base.WeiXinCache;
import com.trionesdev.weixin.base.WeiXinConfig;
import com.trionesdev.weixin.base.ex.WeiXinException;
import com.trionesdev.weixin.offiaccount.WeiXinOfficeAccount;
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

@Configuration(value = "com.trionesdev.weixin.offiaccount.autoconfigure.WeiXinOfficeAccountAutoConfiguration")
@ConditionalOnProperty(prefix = "triones.weixin.offiaccount", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(value = {WeiXinOfficeAccountProperties.class})
@Import(value = {WeiXinOfficeAccountAutoConfiguration.AutoConfiguredRegistrar.class})
public class WeiXinOfficeAccountAutoConfiguration {


    public static class AutoConfiguredRegistrar implements EnvironmentAware, BeanFactoryPostProcessor, ApplicationContextAware {
        private WeiXinOfficeAccountProperties confProperties;
        private ApplicationContext applicationContext;

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
            configurableListableBeanFactory.addBeanPostProcessor(new WeiXinOfficeAccountBeanPostProcessor(confProperties, applicationContext));
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
            WeiXinConfig weiXinConfig = new WeiXinConfig();
            weiXinConfig.setAppId(confProperties.getAppId());
            weiXinConfig.setSecret(confProperties.getSecret());
            weiXinConfig.setMulti(confProperties.getMulti());
            weiXinConfig.setCredentials(confProperties.getCredentials());
            ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
            argumentValues.addIndexedArgumentValue(0, weiXinConfig);
            registerBean(beanFactory, argumentValues, WeiXinOfficeAccount.class.getName());
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.confProperties = Binder.get(environment).bind("triones.weixin.offiaccount", WeiXinOfficeAccountProperties.class).get();
        }

        private void registerBean(DefaultListableBeanFactory beanFactory, ConstructorArgumentValues argumentValues, String beanName) {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(WeiXinOfficeAccount.class);
            beanDefinition.setBeanClassName(WeiXinOfficeAccount.class.getName());
            beanDefinition.setConstructorArgumentValues(argumentValues);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }

    public static class WeiXinOfficeAccountBeanPostProcessor implements BeanPostProcessor {
        private final WeiXinOfficeAccountProperties confProperties;
        private final ApplicationContext applicationContext;

        public WeiXinOfficeAccountBeanPostProcessor(WeiXinOfficeAccountProperties confProperties, ApplicationContext applicationContext) {
            this.confProperties = confProperties;
            this.applicationContext = applicationContext;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof WeiXinOfficeAccount) {
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
                ((WeiXinOfficeAccount) bean).setWeiXinCache(weiXinCache);
            }
            return bean;
        }

    }


}
