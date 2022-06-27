package com.moensun.spring.boot.weixin.offiaccount;

import com.moensun.weixin.commons.WeiXinConfig;
import com.moensun.weixin.offiaccount.OfficeAccount;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
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
import org.springframework.core.env.Environment;

import java.util.Map;

@Configuration(value = "com.moensun.spring.boot.weixin.offiaccount.WeiXinOfficeAccountAutoConfiguration")
@ConditionalOnProperty(prefix = "weixin.offiaccount", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(value = {WeiXinOfficeAccountConfProperties.class})
public class WeiXinOfficeAccountAutoConfiguration implements EnvironmentAware, BeanFactoryPostProcessor {
    private WeiXinOfficeAccountConfProperties confProperties;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
        Map<String, WeiXinOfficeAccountInstanceProperties> instancePropertiesMap = confProperties.getInstance();
        if (MapUtils.isEmpty(instancePropertiesMap)) {
            WeiXinConfig weiXinConfig = new WeiXinConfig();
            weiXinConfig.setAppId(confProperties.getAppId());
            weiXinConfig.setSecret(confProperties.getSecret());
            weiXinConfig.setWeiXinCache(confProperties.getCache());
            ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
            argumentValues.addIndexedArgumentValue(0, weiXinConfig);
            registerBean(beanFactory, argumentValues, OfficeAccount.class.getName());
        } else {
            instancePropertiesMap.forEach((k, v) -> {
                WeiXinConfig weiXinConfig = new WeiXinConfig();
                weiXinConfig.setAppId(v.getAppId());
                weiXinConfig.setSecret(v.getSecret());
                weiXinConfig.setWeiXinCache(v.getCache());
                String baneName = StringUtils.isBlank(v.getName()) ? k : v.getName();
                ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
                argumentValues.addIndexedArgumentValue(0, weiXinConfig);
                registerBean(beanFactory, argumentValues, baneName);
            });
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.confProperties = Binder.get(environment).bind("weixin.offiaccount", WeiXinOfficeAccountConfProperties.class).get();
    }

    private void registerBean(DefaultListableBeanFactory beanFactory, ConstructorArgumentValues argumentValues, String beanName) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(OfficeAccount.class);
        beanDefinition.setBeanClassName(OfficeAccount.class.getName());
        beanDefinition.setConstructorArgumentValues(argumentValues);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

}
