1.AbstractApplicationContext的refresh()方法下的invokeBeanFactoryPostProcessors(beanFactory);
  作用：完成BeanFactoryPostProcessor和BeanDefinitionRegistryPostProcessor完成对这两个接口的调用
    (1)获取实现了BeanDefinitionRegistryPostProcessor接口的所有类的BeanDefinition对象的beanName,对实现类中
       有@order(这是一个实例化排序注解)注解的类进行排序
    (2)BeanFactoryPostProcessor是spring容器级别的拓展接口,是在BeanDefinition加载完成之后,未实例化之前.
       可以拓展接口,对定制化修改BeanDefinition，就是可以在未实例化之前对BeanDefinition做一些操作。
    (3)BeanDefinitionRegistryPostProcessor这个接口是BeanFactoryPostProcessor接口的子接口
    (4)BeanDefinitionRegistryPostProcessor继承自BeanFactoryPostProcessor，是一种比较特殊的BeanFactoryPostProcessor。
       BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry方法可实现自定义的bean注册定义。
       通常spring注册bean使用静态方式, 如:xml、@Bean注解或@Component方式实现注册.不能通过程序来选择是否注册。

       而实现BeanDefinitionRegistryPostProcessor的类可以获得BeanDefinitionRegistry对象，通过它可以动态的注册组件,
       是实现动态注册的钩子函数。spring典型的ConfigurationClassPostProcessor拓展BeanDefinitionRegistryPostProcessor
       解析@Configuration配置类.
2.AbstractApplicationContext的refresh()方法下的registerBeanPostProcessors(beanFactory);这个方法是去实例化所有实现了
    BeanPostProcessor接口的类，从这个实现类中获取BeanDefinition去实例化，还有去实例化MergedBeanDefinitionPostProcessor
    接口的实现类，这个是Spring内部使用的Bean实例化；
  作用：把实现了BeanPostProcessor接口的类实例化，并且加入到BeanFactory中
    (1)实例化的过程中，先寻找当前正在实例化的bean中有@Autowired注解的构造函数，将其实例化，再实例化无参构造函数
    (2)对类中注解过程进行装配，CommonAnnotationBeanPostProcessor支持了@PostConstruct，@PreDestroy,@Resource注，
        AutowiredAnnotationBeanPostProcessor 支持 @Autowired,@Value注解。
        BeanPostProcessor接口的典型运用，这里要理解这个接口，这接口是一个对实例化Bean进行增强的过程。
    (3)初始化之前把beanName添加到singletonsCurrentlyInCreation Set容器中，在这个集合里面的bean都是正在实例化的bean，
        实例化完以后再将beanName从singletonsCurrentlyInCreation容器中删除，并把对象缓存到singletonObjects缓存中,
        bean创建完成时放入一级缓存，删除二级和三级缓存。
    (4)是否单例bean提前暴露,如果是则添加三级缓存。
    (5)DI依赖注入，@Autowired注解实现的依赖注入
    (6)对类中某些特殊方法的调用，比如@PostConstruct(依赖注入Bean后执行一次的方法)，Aware接口
    (7)InitializingBean接口，afterPropertiesSet，init-method属性调用
    (8)最后把实例化的Bean缓存起来

