package com.template;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @author xiewu
 * @date 2023/8/31 10:52
 */
@Component
public class GlobalTemplateDemo extends GlobalTemplate<ParamsDTO, MethodDTO, ResultVO>{

    @Resource
    private Executor executor;

    @Override
    void validation(ParamsDTO params) {
    }

    @Override
    ResultVO assembleData(MethodDTO methodDTO) {
        //这里可以直接new一个ResultVO，或者和下面一样把ResultVO放入到MethodDTO类中，可以根据实际业务场景调整
        return methodDTO.getResultVO();
    }

    public void nextHandle() {
        //异步执行
        executor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void nextHandle1() {

    }
}
