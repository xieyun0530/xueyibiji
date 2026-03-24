1.getaway网关的三大组件
    (1)routes：id和uri，id是唯一的，uri是路由地址；
    (2)predicates：断言也就是路由的条件；
    (3)filter：路由分为pre过滤和post过滤，pre路由是通过断言规则后进行的过滤，post过滤是返回结果后的过滤；
2.路由规则
    spring:
      application:
        name: gateway9528
      cloud:
        gateway:    # 所有的路由映射这里配置
          routes:
            - id: order8102 # 路由ID
              uri: http://localhost:8102 # 路由的URI
              predicates:
                - Path=/get/gateway # 路由的路径
    #            - After=2020-10-30T15:00:22.432+08:00[Asia/Shanghai] #在该时区后发生
    #            - Before=2020-10-30T16:00:22.432+08:00[Asia/Shanghai] #在该时区前发生
    #            - Between=2020-10-30T15:00:22.432+08:00[Asia/Shanghai],2020-10-30T16:00:22.432+08:00[Asia/Shanghai] #在两个时区内发生
    #            - Cookie=username,lisi #携带cookies信息，相当于键值对，username为key，lisi为value
    #            - Cookie=username,\d+ #携带cookies信息，相当于键值对，username为key，\d+为value，是一个正则表达式，表达为正数
    #            - Header=id,001 #携带header信息，相当于键值对，id为key，001为value
    #            - Header=id,\d+ #携带header信息，相当于键值对，id为key，\d+为value，是一个正则表达式，表达为正数
    #            - Host=**.somehost.org,**.anotherhost.org #host匹配，**为通配符
    #            - Method=GET,POST #允许的请求方法
    #            - Query=number,abc #带查询条件，第一个是查询参数，第二个是可选的值
    #            - Query=number,\d+ #带查询条件，第一个是查询参数，第二个是可选的正则表达式
            filters:
            (1)path类型的过滤器
                #对于/ foo / bar的请求路径，这将在发出下游请求之前将路径设置为/ bar。 注意由于YAML规范，$ \替换为$
              - RewritePath=/foo/(?<segment>.*), /$\{segment}
                #当通过网关向/name/bar/foo发出请求时，对nameservice的请求将类似于http://nameservice/foo。分割去除前面2个/的参数
              - StripPrefix=2
                #对于/ foo / bar的请求路径，这将在发出下游请求之前将路径设置为/ bar。
                #predicates:
                   #- Path=/foo/{segment}
                #需要和断言配合使用
              - SetPath=/{segment}
            (2)参数过滤器
                #在请求中添加参数foo=bar
              - AddRequestParameter=foo, bar
            (3)响应状态码修改
                #在任何一种情况下，响应的HTTP状态都将设置为401。
              - SetStatus=401

3.fitler路由的解释：https://www.cnblogs.com/liukaifeng/p/10055863.html
  predicats断言的解释：https://blog.csdn.net/weixin_44009447/article/details/109390229

4.自定义网关过滤器GlobalFilter
https://blog.csdn.net/zouliping123456/article/details/116128179

package com.changgou.filter;

@Slf4j
@Component
public class CustomGatewayFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.info("自定义网关过滤器...");
        return chain.filter(exchange);// 继续向下执行
    }
    //过滤器顺序，数组越小顺序越靠前
    @Override
    public int getOrder() {
        return 0;
    }
}

package com.example.config;

import com.example.filter.CustomGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;·
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关路由配置类
 */
@Configuration
public class GatewayRoutesConfiguration {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes().route(r -> r
                // 断言（判断条件）
                .path("/product/**")
                // 目标 URI，路由到微服务的地址
                .uri("lb://ai-product")
                // 注册自定义网关过滤器
                .filters(new CustomGatewayFilter())
                // 路由 ID，唯一
                .id("product"))
                .build();
    }

}


