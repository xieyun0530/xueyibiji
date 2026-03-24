package com.eh;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(RocketMQProducer.class)
public class ProductEnhancerConfig {

    @Primary
    @Bean
    public Producer producer1(Producer producer){
        return new ProductEnhancer(producer);
    }
}
