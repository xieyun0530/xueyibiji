package com;

import com.event.MyApplicationEvent;
import com.event.MyApplicationListener;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * Created by vn0790t on 2020/1/9.
 */
@SpringBootApplication
public class DesignPatternDemoApplication {

    public List<byte[]> testShow(){
        List<byte[]> aList = Lists.newArrayList();
        aList.add(new byte[502400]);
        return aList;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DesignPatternDemoApplication.class, args);
        //添加监听者，其实也可以理解成收听者，也就是接收事件发布消息的
        applicationContext.addApplicationListener(new MyApplicationListener());
        /**
         * 1.发布事件(发布事件让对应的监听者执行onApplicationEvent)，也就是会去执行MyApplicationListener中的onApplicationEvent方法
         * 2.这里会到Listener(收听者)集合中找到类型是MyApplicationEvent(事件)的收听者类，然后调用他的onApplicationEvent方法
         */
        applicationContext.publishEvent(new MyApplicationEvent(new Object()));
    }
}
