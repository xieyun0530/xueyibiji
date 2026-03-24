package com.template;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.stream.Stream;

/**
 * 业务处理模版类测试类
 * @author xiewu
 * @date 2023/8/31 10:58
 */
@RestController
@RequestMapping("/templateTestDemo")
public class TemplateControllerDemo {

    @Resource
    private GlobalTemplateDemo globalTemplateDemo;

    @RequestMapping("/test")
    public ResultVO test(@RequestBody ParamsDTO paramsDTO){
        globalTemplateDemo.setFunctions(Stream.<Runnable>builder()
                .add(globalTemplateDemo::nextHandle)
                .add(globalTemplateDemo::nextHandle1).build());
        ResultVO resultVO = globalTemplateDemo.handle(paramsDTO);
        return resultVO;
    }
}
