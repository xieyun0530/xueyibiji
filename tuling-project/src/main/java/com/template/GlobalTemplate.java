package com.template;

import lombok.Data;

import java.util.stream.Stream;

/**
 * 业务处理模版类
 * @param <P> 校验参数对象
 * @param <M> 方法间传递设置参数对象
 * @param <R> 最终返回对象
 */
@Data
public abstract class GlobalTemplate<P, M, R> {

    /**
     * 方法之间传递的对象
     */
    private M methodParams;

    /**
     * 无事务业务处理方法
     */
    public Stream<Runnable> functions;

    /**
     * 业务处理方法
     * @return
     */
    public R handle(P params) {
        // 1. 校验
        if (null != params) validation(params);
        // 2. 查询
        if (null != functions) functions.forEach(r -> r.run());
        // 3. 组装
        return assembleData(getMethodParams());
    }

    /**
     * 参数校验方法， 直接抛出异常
     * @param params
     */
    abstract void validation(P params);

    /**
     * 最终组装数据方法，可在重写处添加事务，如有事务需求，需全写在该方法内
     * @param methodParams
     * @return
     */
    abstract R assembleData(M methodParams);
}
