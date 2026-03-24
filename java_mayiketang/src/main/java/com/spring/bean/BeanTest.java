package com.spring.bean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(UserInfo.class);
        UserInfo userInfo = (UserInfo) applicationContext.getBean("userInfo");
    }
}
