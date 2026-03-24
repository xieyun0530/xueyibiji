1.dubbo的配置解析过程
    (1)将dubbo自定义标签解析成BeanDefinition，这里使用的是和Spring中解析自定义标签的方式是一样的，同样使用的是SPI；
    (2)dubbo中使用xml配置的解析过程
        1.在dubbo-config-->dubbo-config-spring项目中的src/main/resouce/META-INF下有一个dubbo.xsd文件，该文件是用来
          描述spring标签的，也就是回描述自定义标签的一些格式是否正确；也可以理解成标签的约束文件；
        2.spring.shemas文件，则是为了方便的找到dubbo.xsd，因为约束文件默认是到网络上搜索的，有了这个配置文件就可以
          将网络搜索重定向到本地，这个重定向是由spring来完成的；
        3.spring.handlers文件，则是定义了解析Spring自定义标签的解析实现类；
        4.spring.handlers文件文件有一个DubboNamespaceHandler的实现类，该类在实例化的时候回调用init方法，该方法会
          按照dubbo标签的属性，实例对应的实现类然后放入到一个Map容器中key为dubbo属性值value为属性对应的处理类，SpringIOC
          容器在解析标签的时候，会根据不同的标签去get对应的实现类，这些实现类都实现了BeanDefinitionParser接口，该接口
          有一个parse方法，这个方法就是具体去解析将标签信息解析成BeanDefinition，最后spring会去实例化这些BeanDefinition
          将BeanDefinition编程bean放入到缓存中。
    (3)dubbo注解的解析过程
        1.使用@EnableDubbo注解用于启动dubbo配置，这个注解中又包含了@EnableDubboConfig和@DubboComponentScan注解
        2.@EnableDubboConfig注解用于处理dubbo的全局组件的配置，一般是配置在.properties文件中，全局配置有如下配置：
          dubbo.application：服务名
          dubbo.registry：注册中心，如zk
          dubbo.protocol：dubbo中消费与生产者之间的通讯协议，由生产者指定，消费者被动接受
          dubbo.provider：生产者
          dubbo.consumer：消费者
          dubbo.monitor：监听(可选)
          dubbo.module：用于配置模块信息(可选)
          上面5中是基础必要配置，最后2种是可选配置
        3.@DubboComponentScan注解，用来解析业务类种的@Service注解的实现，主要是暴露@Service和引用服务@Reference,
          处理逻辑在@Import(DubboComponentScanRegistrar.class)中的DubboComponentScanRegistrar类中，进入
          registerBeanDefinitions()方法，该方法里面会调用有两个方法分别是：registerServiceAnnotationBeanPostProcessor()
          和registerReferenceAnnotationBeanPostProcessor();
          registerServiceAnnotationBeanPostProcessor()方法：
          该方法该方法中会注入一个ServiceAnnotationBeanPostProcessor类，这个类是用来将标注了@Service的实现类暴露出去的，
          相当于dubbo中<dubbo:service/>标签用来暴露实现类，这个类继承了BeanDefinitionRegistryPostProcessor接口，这个接口是
          一个spring的后置处理器接口，实现了该接口的类会在refresh的invokeBeanFactoryPostProcessors方法中调用到，
          会调用该接口的postProcessBeanDefinitionRegistry()方法，这个方法就回去扫描@DubboComponentScan注解规定的
          包下带有@Service注解的类和类属性中带有@Reference的注解(这里@Reference注解标注的属性所在的类需要使用
          @Component注解把该类注入到SpringIOC容器中);
          进入postProcessBeanDefinitionRegistry()方法-->registerServiceBeans()-->registerServiceBean()
          -->buildServiceBeanDefinition()使用builder.getBeanDefinition()创建ServiceBean该类实现了ApplicationContextAware
          接口，因此ServiceBean持有一个SpringIOC的ApplicationContext上下文对象，使用这个上下文对象可以去获取SpringIOC
          容器中Bean，addPropertyReference(builder, "ref", annotatedServiceBeanName)方法中，serviceBean就会将ref指向
          业务bean，这个是服务端暴露服务的过程，将bean暴露出去;
          registerReferenceAnnotationBeanPostProcessor();方法：
          该方法中会注入一个ReferenceAnnotationBeanPostProcessor类-->父类AnnotationInjectedBeanPostProcessor这个类实现了
          MergedBeanDefinitionPostProcessor接口，方法postProcessMergedBeanDefinition()在创建bean实例前会被调用
          （用来找出bean中含有@Reference注解的Field和Method），最终从postProcessMergedBeanDefinition()---》
          postProcessPropertyValues()该方法中的findInjectionMetadata()会找出reference标注的所有field和method，然后
          使用metadata.inject(bean, beanName, pvs)对字段和方法进行反射绑定；
          当Spring完成bean的创建后会调用AbstractAutowireCapableBeanFactory#populateBean方法完成属性的填充；
2.dubbo的服务暴露

3.dubbo的invoke机制
  (1)服务端：
     1.实现一个invoke接口，将要暴露的接口、接口的实现类，以及url(类似rmi://127.0.0.1:9001/com.enjoy.service.DemoService)
       放入到这个实现类中，这个invoke中接口的参数是一个Invocation接口，这个接口的实现类是RpcInvocation类，这个里面就是
       存入MethodName(调用的方法名)、ParameterTypes(参数类型)、Arguments(方法中的参数)，其实这里的invoke方法所做的事情就是
       将Invocation中的参数去出来，进行反射调用实现类的一个过程，也就是一个实现类的调用，消费者就可以只需要传入一些参数，
       就能够在这个方法里面进行一个反射调用，其实就可以认为服务端定义的一个反射调用实现类的方法；
     2.服务端就会将这个反射调用实现类的方法的实例放入到protocol的协议接口的协议中，这个protocol是一个SPI的接口，所以协议也是
       可以实现不同的协议，dubbo这样做的好处就是，进行解耦，你可以在不改变其它代码的情况下，根据这个接口实现不同的协议；
     3.实例化协议对象后，将反射调用实现类的实例赋给这个协议，这个协议里面会将这个反射调用的实例对象，做一层代理，再将这个
       代理对象暴露出去；dubbo中使用的是动态编译的静态代理方式；
     4.当protocol协议接受到请求后，就会进入到第三步中protocol协议里面创建的代理类的invoke方法，而这个代理类则就会去调用
        在第一步中服务端实现类的invoke接口实现类中的invoke方法，这个方法就会根据消费端传过来的参数去反射调用实现类的具体
        的method，然后如果有返回值则会返回一个返回值；
        总结：
            这里protocol中为什么需要使用一个代理类来将调用具体的实现类的逻辑抛给invoke接口的？
            因为为了让服务端不同的协议的入口统一成invoke.invoke方法，而如果直接调用实现类的方法的话，这样耦合性太强了，
            需要实现了很多实现类的方法。


















