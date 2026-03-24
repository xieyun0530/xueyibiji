package com.spring;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.Executors;

/**
 * Created by vn0790t on 2020/6/2.
 */
public class TestMain {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(MySpringConfig.class);

//        System.out.println("------->>>>打印所有的IOC实例");
//        String[] beanDefinitionNames = context.getBeanDefinitionNames();
//        for (String name : beanDefinitionNames) {
//            System.out.println(name);
//        }
//        BeanFactory
 
//        UserEntity userEntity = context.getBean("userEntity", UserEntity.class);
//        System.out.println("获取到实例：" + userEntity.toString());

        AopService aopService = context.getBean("aopService", AopService.class);
        aopService.test();

        Object ob = context.getBean("testFactoryBean");
        System.out.println(ob);
    }
}
