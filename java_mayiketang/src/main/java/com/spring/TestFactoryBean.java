package com.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: xiewu
 * @Date: 2021/12/23
 * @Time: 13:00
 */
@Component
public class TestFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {

        return new AopService();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
