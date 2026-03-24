1.spring的IOC和AOP
  (1)IOC与DI
    什么是IOC?
    控制反转，将bean对象交给spring容器进行管理
    使用反射机制和dom4j解析xml文件实现的
    什么是DI
    依赖注入，解决对象之间的依赖
  (2)spring创建bean生命周期
    单例-----jvm只能与运行存在一次
    多例-----每次运行都会创建一次
    request-----请求作用域
    session-----对象session绑定管理
    spring默认是单例
  (3)单例
    怎么证明对象是单例
    使用构造函数；单例无参构造函数只会执行一次；然后获取两次bean进行对比，等于true就是单例。
    使用单例要注意什么
    注意共享变量的线程安全问题
    单例的实现方式：饿汉式和懒汉式
    spring默认是单例企且使用的是饿汉式
  (4)多例
    无参构造函数会执行多次，且线程安全
  (5)@Autowired与@Resource的区别
    @Autowired使用类型查找
    @Resource使用名称查询找，是JDK1.6支持的注解；注解写在setter方法上默认取属性名进行装配；当找不到与名称匹配的bean时才按照类型进行装配。
  (6)什么是是代理设计模式
   作用：提供对目标对象访问的方式（中介）
  (7)静态代理与动态代理的区别
    静态代理需要生成代理类
    动态代理不需要生成代理类，动态代理分为：jkd动态代理(使用反射机制实现)，cglib动态代理(使用字节码实现)
  (8)什么是SpringAOP
    面向切面编程----是一种思想
    应用场景：权限控制、事务管理、日志打印、性能统计
    Aop 关注点：重复代码；切面：抽取重复代码；切入点：拦截那些方法
    AOp分为：前置通知、后置通知、运行通知、异常通知、环绕通知
2.Spring事务
  (1)什么事务？
    事务的特性：
    原子性：那么全部成功，要么全部失败，不可再分
    一致性：事务前后的总金额相等
    隔离性：多个事务互不影响
    持久性：数据持久化到数据库后不会再改变
    Spring事务分类：
    手动事务：----自己begin、commit；aop编程实现自动化事务
    声明式事务：xml方式、注解方式@transaction
    也可以说：编程事务（手动事务）----声明事务（xml和注解）
  (2)事务实现原理
    Aop编程+环绕通知+异常通知
  (3)事务的传播行为
   支持当前事务，如果当前没有事务，新建一个事务，常见选择。(propagation_required)
   支持当前事务，如果当前没有事务，就以非事务方式运行。(propagation_supports)
   支持当前事务，如果当前没有事务，就抛出异常。(propagation_mandatory)
   新建事务，如果当前存在事务，把当前事务挂起。(propagation_requires_new)
   以非事务方式执行，如果当前存在事务，把当前事务挂起。(propagation_not_supported)
   以非事务方式运行，如果当前存在事务，则抛出异常。(propagation_never)
   如果当前存在事务，则在嵌套事务内执行，如果当前没有事务，则执行与propagation_required类似的操作。
   面试回答四种就行了：propagation_required、propagation_requires_new、propagation_supports、propagation_not_supported
  (4)Bean的生命周期
    简化版
    1.创建bean（实例化），使用反射机制
    2.初始化属性（属性注入bean）
    3.BeanNameAware获取查找beanName（beanId）
    4.BeanFactoryAware---获取bean容器工厂
    5.获取上下文 ApplicationContextAware
    6.BeanPostProcess
    7.销毁bean
    完整的加载过程
    1.Spring对Bean进行实例化（相当于程序中的new Xx()）
    2.Spring将值和Bean的引用注入进Bean对应的属性中
    3.如果Bean实现了BeanNameAware接口，Spring将Bean的ID传递给setBeanName()方法 
    （实现BeanNameAware清主要是为了通过Bean的引用来获得Bean的ID，一般业务中是很少有用到Bean的ID的）
    4.如果Bean实现了BeanFactoryAware接口，Spring将调用setBeanDactory(BeanFactory bf)方法并把BeanFactory容器实例作为参数传入。 
    （实现BeanFactoryAware 主要目的是为了获取Spring容器，如Bean通过Spring容器发布事件等）
    5.如果Bean实现了ApplicationContextAwaer接口，Spring容器将调用setApplicationContext()方法，把bean所在的应用上下文的引用传入. 
    (作用与BeanFactory类似都是为了获取Spring容器，不同的是Spring容器在调用setApplicationContext方法时会把它自己作为setApplicationContext
     的参数传入，而Spring容器在调用setBeanDactory前需要程序员自己指定（注入）setBeanDactory里的参数BeanFactory )
    6.如果Bean实现了BeanPostProcess接口，Spring将调用它们的postProcessBeforeInitialization（预初始化）方法 
    （作用是在Bean实例创建成功后对进行增强处理，如对Bean进行修改，增加某个功能）
    7.如果Bean实现了InitializingBean接口，Spring将调用它们的afterPropertiesSet方法，
      作用与在配置文件中对Bean使用init-method声明初始化的作用一样，都是在Bean的全部属性设置成功后执行的初始化方法。
    8.如果Bean实现了BeanPostProcess接口，Spring将调用它们的postProcessAfterInitialization（后初始化）方法
      （作用与6的一样，只不过6是在Bean初始化前执行的，而这个是在Bean初始化后执行的，时机不同 )
    9.此时，bean已经准备就绪，可以被程序使用了，Bean将一直驻留在应用上下文中给应用使用，直到应用上下文被销毁
    10.如果Bean实现了DispostbleBean接口，Spring将调用它的destory方法，作用与在配置文件中对Bean使用destory-method属性的作用一样，
       都是在Bean实例销毁前执行的方法。

  (5)Spring上下问的作用
    获取当前容器对象；
    过滤器中，过去bean对象，能获取到吗？
    不能，因为过滤器不在扫包范围，可以使用Spring上下文对象区获取bean
    
3.SpringAop
   (1)SpringAop的核心类
    AnnotationAwareAspectJAutoProxyCreator
    1）	spring 容器启动，每个bean的实例化之前都会先经过AbstractAutoProxyCreator类的postProcessAfterInitialization（）这个方法，
    然后接下来是调用wrapIfNecessary方法。