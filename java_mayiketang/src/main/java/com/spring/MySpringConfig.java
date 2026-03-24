package com.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by vn0790t on 2020/6/2.
 */
@Configuration
@ComponentScan(value = {"com.spring"})
@EnableAspectJAutoProxy
public class MySpringConfig {

    @Bean
    public UserEntity userEntity() {
        return new UserEntity(1, "zhangsan");
    }
}
