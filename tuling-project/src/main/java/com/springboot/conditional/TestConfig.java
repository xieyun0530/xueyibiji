package com.springboot.conditional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(MyCondition.class)
public class TestConfig {

    @Bean
    // 注入Bean之前增加限制条件：MyCondition，条件满足才会构造TestBean同时注入
//    @Conditional(MyCondition.class)
    public TestBean testbean() {
        System.out.println("=====run new TestBean");
        TestBean testBean = new TestBean();
        testBean.setId(1L);
        return testBean;
    }
}

class TestBean {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "id=" + id +
                '}';
    }
}
