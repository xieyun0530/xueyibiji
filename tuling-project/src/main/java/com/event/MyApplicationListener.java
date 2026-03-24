package com.event;

import org.springframework.context.ApplicationListener;

public class MyApplicationListener implements ApplicationListener<MyApplicationEvent> {

    /**
     * 这里其实就是说只有参数是MyApplicationEvent的时候才能收到监听
     *
     * @param event
     */
    public void onApplicationEvent(MyApplicationEvent event) {
        System.out.println("接收到事件：" + event.getSource());
    }

}