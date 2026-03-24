package com.event;
 
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
 
@Component
public class MyEventHandle {
 
 
    /**
     * 参数任意(为Object）的时候所有事件都会监听到
     * 所有，该参数事件，或者其子事件（子类）都可以接收到
     */
    @EventListener
    public void event(Object event){
        System.out.println("MyEventHandle 接收到事件：" + event.getClass());
    }
    
}