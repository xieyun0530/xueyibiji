package com.eh;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 子类继承父类把父类进行增强相当于代理
 */
@Service
public class TestProduct implements ApplicationRunner {

    @Resource
    private Producer producer;

    @Resource
    @Qualifier(value = "producer")
    private RocketMQProducer rocketMQProducer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("最新producer：" + producer);
        producer.show();
        System.out.println("最新rocketMQProducer：" + rocketMQProducer);
        rocketMQProducer.show();
    }
}
