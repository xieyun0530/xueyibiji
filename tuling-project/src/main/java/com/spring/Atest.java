package com.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 测试Autowired和resources注解的区别
 * Autowired默认先安装类型装配，如果有多个则再安装名称装配
 * resources先安装名称装配，再安装类型装配
 * @author xiewu
 * @date 2022/10/10 20:53
 */
@Component
public class Atest {
    @Autowired
    private AInterface aInterface;
}
