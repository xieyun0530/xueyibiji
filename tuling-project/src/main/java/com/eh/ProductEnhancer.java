package com.eh;


public class ProductEnhancer implements Producer{

    private Producer producer;

    public ProductEnhancer(Producer producer) {
        System.out.println("旧新product方法：" + producer);
        this.producer = producer;
    }

    @Override
    public void show() {
        System.out.println("rockerMQ show 增强");
        producer.show();
    }
}
