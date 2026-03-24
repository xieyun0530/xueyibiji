package com.jdbc;

import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 时间轮定时器，每添加一次时间轮任务只会执行一次
 */
@RestController
@RequestMapping("/timer")
public class HashedWheelController {

    @Resource
    private HashedWheelController hashedWheelController;

    //时间轮的定义
    HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(
            new DefaultThreadFactory("demo-timer"),
            100, TimeUnit.MILLISECONDS, 1024, false);

    /**
     * 添加延迟任务
     * @param delay
     */
    @GetMapping("/{delay}")
    public void tick(@PathVariable("delay") Long delay) {
        //SCHEDULED(定时执行的线程）
        //Timer(Java原生定时任务执行）
        System.out.println("CurrentTime:" + new Date());
        hashedWheelTimer.newTimeout(timeout -> {
            System.out.println("Begin Execute:" + new Date());
            hashedWheelController.tick(delay);
        }, delay, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        Redisson redisson = null;
        RLock lock = redisson.getLock("");
        lock.lock();
    }

}
