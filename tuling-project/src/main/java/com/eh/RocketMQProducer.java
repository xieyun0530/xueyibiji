package com.eh;

import org.springframework.stereotype.Service;

@Service
public class RocketMQProducer implements Producer{
    @Override
    public void show() {
        System.out.println("rockerMQ show");
    }
}
