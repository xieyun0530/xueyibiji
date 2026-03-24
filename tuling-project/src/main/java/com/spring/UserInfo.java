package com.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserInfo implements BeanNameAware, BeanFactoryAware, BeanPostProcessor, DisposableBean {

    private String userName;
    private int age;

    public UserInfo(String userName, int age) {
        this.userName=userName;
        this.age=age;
    }
    public UserInfo() {
        System.out.println("1.无参构造函数.....");
    }

    /*@Bean
    public UserInfo userEntity() {
        System.out.println("2.无惨构造函数.....");
        return new UserInfo("zhangsan",1);
    }*/

    @Override
    public void setBeanName(String name) {
        System.out.println("2.BeanName:" + name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("3.setBeanFactory");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("4.postProcessBeforeInitialization bean初始化之前" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("5.postProcessAfterInitialization bean初始化之后" + beanName);
        return bean;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy 销毁bean");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
