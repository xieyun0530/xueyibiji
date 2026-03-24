package com.springboot.conditional;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;

public class MyCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //context能够获取到IOC相关的信息、对象

        //获取ioc使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        //获取当前环境信息
        Environment environment = context.getEnvironment();
        //获取bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();

        //metadata能取到注解的元信息
//        metadata.getAnnotations().forEach(a -> {
//            //注解的class
//            Class<Annotation> type = a.getType();
//            //注解对应的attribute
//            Object value = a.getValue("value").get();
//        });
        //返回false表示未满足条件，不进行构造和注入；返回true表示满足条件，正常构造和注入
        return true;
    }
}
