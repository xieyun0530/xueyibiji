package com.event;

import org.springframework.context.ApplicationEvent;

/**
 * 定义事件
 *
 */
public class MyApplicationEvent extends ApplicationEvent {
 
    private static final long serialVersionUID = 1L;

    /**
     * 这里的source可以根据需求自定义入参，作为onApplicationEvent放入参对象中的source属性
     * @param source
     */
    public MyApplicationEvent(Object source) {
        super(source);
    }
}