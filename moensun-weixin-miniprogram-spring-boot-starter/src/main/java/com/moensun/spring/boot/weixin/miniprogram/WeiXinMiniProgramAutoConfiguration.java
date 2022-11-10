package com.moensun.spring.boot.weixin.miniprogram;

import com.moensun.weixin.commons.WeiXinConfig;
import com.moensun.weixin.miniprogram.MiniProgram;
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

@Configuration(value = "com.moensun.spring.boot.weixin.miniprogram.WeiXinMiniProgramAutoConfiguration")
@ConditionalOnProperty(prefix = "weixin.miniprogram", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(value = {WeiXinMiniProgramConfProperties.class})
public class WeiXinMiniProgramAutoConfiguration implements EnvironmentAware, BeanFactoryPostProcessor {

    private WeiXinMiniProgramConfProperties confProperties;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
        Map<String, WeiXinMiniProgramInstanceProperties> instancePropertiesMap = confProperties.getInstance();
        if (MapUtils.isEmpty(instancePropertiesMap)) {
            WeiXinConfig weiXinConfig = new WeiXinConfig();
            weiXinConfig.setAppId(confProperties.getAppId());
            weiXinConfig.setSecret(confProperties.getSecret());
            weiXinConfig.setWeiXinCache(confProperties.getCache());
            ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
            argumentValues.addIndexedArgumentValue(0, weiXinConfig);
            registerBean(beanFactory, argumentValues, MiniProgram.class.getName());
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
        this.confProperties = Binder.get(environment).bind("weixin.miniprogram", WeiXinMiniProgramConfProperties.class).get();
    }

    private void registerBean(DefaultListableBeanFactory beanFactory, ConstructorArgumentValues argumentValues, String beanName) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(MiniProgram.class);
        beanDefinition.setBeanClassName(MiniProgram.class.getName());
        beanDefinition.setConstructorArgumentValues(argumentValues);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

}
