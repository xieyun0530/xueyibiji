1.SpringBoot在main方法中完成启动的原因？
    (1)SPI
        SPI会在SpringBoot项目中去读取META-INF/spring.factories目录的配置文件内容，把配置文件中的类加载到Spring
        容器中；如果我们自己也想把一个类加载到Spring容器中也可以使用这种方式去做，把配置类放入到spring.factories
        配置文件中即可；
        一个类加载到Spring容器中管理有几种方式：
            1.通过XML加载到Spring容器中的Bean
            2.通过@Component注解被@ComponentScan扫描注入的
            3.通过spring.factories配置类
            前两者是加载本工程的 bean，扫描本工程的 bean，第三点可以加载第三方定义的jar包中的bean，
            毕竟第三方jar包的包名跟本工程包名可能不一样，所以 前两个方式扫描不到。
    (2)创建springBoot的上下文对象
        1.进入SpringApplication.run(SpringbootTest.class,args);的方法中
        2.context = createApplicationContext();创建一个上下文对象AnnotationConfigServletWebServerApplicationContext对象；
        3.createApplicationContext()方法中使用反射的方式会去反射创建AnnotationConfigServletWebServerApplicationContext
          这个上下对象，在这个类的构造函数中会调用register(annotatedClasses);方法将ConfigurationClassPostProcessor变成
          beanDefinition对象；
        4.进入AnnotationConfigServletWebServerApplicationContext的构造方法，这里会实例化两个类
            //读取器
            this.reader = new AnnotatedBeanDefinitionReader(this);
            //扫描器
            this.scanner = new ClassPathBeanDefinitionScanner(this);
        5.实例化好Spring上下文对象后 ，SpringApplication.run(SpringbootTest.class,args);方法中，调用refreshContext(context);
           方法，该方法就会去调用AbstractApplicationContext类中的refresh();方法；
           这里AnnotationConfigServletWebServerApplicationContext上下文对象继承了ServletWebServerApplicationContext类，
           而ServletWebServerApplicationContext类又继承了GenericWebApplicationContext类，该类有继承了GenericApplicationContext
           该类又继承了AbstractApplicationContext；
           所里这里使用((AbstractApplicationContext) applicationContext).refresh();强转成了父类AbstractApplicationContext，
           然后调用这个父类中refresh()方法
        6.这个refresh()方法就是就是一个将注解或者xml中配置的bean解析并注入到Spring容器中的一个核心方法，在SpringBoot中一般都是
          使用注解的方式，所以这里会进入refresh方法中的invokeBeanFactoryPostProcessors方法中，这个方法的作用是调用使用了
          BeanFactoryPostProcessor接口BeanDefinitionRegistryPostProcessor的后置处理器的实现类，这里则会去调用
          ConfigurationClassPostProcessor这个类，因为这个类继承了BeanDefinitionRegistryPostProcessor这个接口，这里会去调用
          这个实现类的postProcessBeanDefinitionRegistry()方法，而这个方法里面就是去解析，解析到@ComponentScan这个扫描器注解
          去扫描这个注解指定报下的注解将其解析成BeanDefinition然后放入到Spring容器中；
        6.这里refresh方法中会有一个onRefresh();方法，这个方法是一个模板设计模式的钩子方法，这个方法就是用来在SpringBoot启动的
          时候启动Tomcat的，因为AnnotationConfigServletWebServerApplicationContext上下文对象继承了
          ServletWebServerApplicationContext类，该类的父类的父类继承了AbstractApplicationContext上下文对象，这个类里面去
          实现类这个onRefresh()钩子方法，这个钩子方法里面调用createWebServer()去创建一个Tomcat，这里主要可以看
          factory.getWebServer(getSelfInitializer());这个方法；这里方法会根据springboot中的Pom中提供的不同的启动类去创建不同
          的启动器，但是如果pom文件中没有其它的启动容器的话，就会使用默认的也就是Tomcat容器；
2.SpringBoot自动配置的源码分析
    (1)为什么有SpringBoot自动配置功能？
       在springboot项目中，我们可以在业务代码里面用事务注解，用缓存注解，用mvc相关的功能等等，但是我们并没有在springboot
       项目把这些功能开启添加进来，那么为什么我们可以在业务代码中使用这些功能呢？也就是说这些功能如何跑到springboot项目中来的呢？
       这就是springboot的自动配置功能。
       总结就是约定大于配置，那这个约定就是一个自动配置的功能。
    (2)进入@SpringBootApplication--》@EnableAutoConfiguration这个注解中使用@Import({AutoConfigurationImportSelector.class})
       注入了一个AutoConfigurationImportSelector类,这个类实现了DeferredImportSelector接口；
       接下类分析下这个AutoConfigurationImportSelector类使用SPI来获取配置类的过程：
        1.首先AutoConfigurationImportSelector这个类是使用@Import注解注入到Spring容器中，这里因为这个类继承了DeferredImportSelector，
          而这个接口又继承了ImportSelector这个接口中有一个selectImports(AnnotationMetadata importingClassMetadata)有参方法，
          在Spring扫描到@Import注解后，会去获取注入的类，然后会看这个类如果有继承这个接口，这会调用这个有参构造函数。
        2.所以我们进入selectImports方法，这个方法中的getAutoConfigurationEntry(autoConfigurationMetadata,annotationMetadata);
          是用来获取所有的自动配置类的，进入这个方法会看到getCandidateConfigurations(annotationMetadata, attributes)方法，
          这个方法就是使用SPI获取EnableAutoConfiguration为key的所有实现类的方法；
        3.进入到这个方法中，会看到如下的调用，
          SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),getBeanClassLoader());
          --》loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList())
          这个方法就会去MATE-INF文件下加载spring.factories中EnableAutoConfiguration为key的所有类的所有实现类；这里的现实类
          就是一些自动配置的实现类；
        (4)AutoConfigurationImportSelector类中process()方法和selectImports()这两个方法被ConfigurationClassPostProcessor调用到，
           这个整个调用过过程为：在refresh()方法的invokeBeanFactoryPostProcessors()方法--》invokeBeanFactoryPostProcessors()
           --》invokeBeanDefinitionRegistryPostProcessors()这里面会有一个for循环会去调用实现了BeanDefinitionRegistryPostProcessor
           接口的实现类，在这里就会调用到ConfigurationClassPostProcessor这个实现类的postProcessBeanDefinitionRegistry()方法，
           --》processConfigBeanDefinitions()--》parser.parse(candidates)--》parse()-->processConfigurationClass()
           --》doProcessConfigurationClass()--》processImports()--》handle()--》processGroupImports()--》getImports()
           getImports()这个方法里面就去会调用process()方法和selectImports()这两个方法；
3.AOP功能的自动配置类
     在MATE-INF/spring.factories文件中的key的所有类中，有一个AopAutoConfiguration类，这个类就是一个引入AOP功能的类，该类中
     有一个@EnableAspectJAutoProxy(proxyTargetClass = true)注解，加了这个注解就默认开启了aop功能，这里默认是开了的cglib代理；
     这里面会有@ConditionalOnProperty(prefix = "spring.aop", name = "auto", havingValue = "true", matchIfMissing = true)注解，
     从这个注解havingValue = "true"就知道，默认使用的是cglib代理；
     这个prefix = "spring.aop"就是表示，在配置文件中加载以spring.aop开头的配置；
4.Conditional功能的实现
    (1)Conditional功能的使用很简单使用的需求是：有的时候我们需要当某一个条件满足的时候才把一些类实例化并加入到spring 容器中。
    (2)@ConditionalOnBean(name="jack")当spring容器中存在jack这个bean时，才调用加了该注解的方法注入Bean；
    (3)@ConditionalOnClass(name="com.xiangxue.jack.controller.JackController")/@ConditionalOnClass(name="jack")
       当类路径不存在该类时，才会调用加了该注解的方法；
    (4)@ConditionalOnExpression("${spring.datasource.max-idle}==10")当表达式成立时调用该方法;
    (5)@ConditionalOnProperty(prefix = "spring.redis"，name="host"，havingValue="192.168.67.139")当配置文件的值相等时调用该方法;
    (6)自定义condition，实现Condition接口，自定义condition的使用@Conditional(value="实现了condition接口的实现类".class)
    (7)Conditional的原理和源码
        1.从condition的使用需求我们知道，这个是单条件满足的时候才实例化bean和加入到spring容器，而在spring中一个类的实例化
          必须要变成beanDefinition对象。而ConfigurationClassPostProcessor是所有 beanDefinition 对象的集散地，所有的
          beanDefinition 都会在这个类里面处理。那么我们要完成Condition功能也必定在这个类里面。
        2.在ConfigurationClassPostProcessor类的processConfigBeanDefinitions()-->parser.parse(candidates);-->parse();
          -->processConfigurationClass()-->shouldSkip()-->condition.matches(this.context, metadata))
          这个方法是在SpringBootCondition类中，所以进入该类的matches()方法中，该方法中有一个
          getMatchOutcome(context, metadata);方法，这个方法是一个钩子方法，根据不同的子类实现不同的注解，
          注解：@ConditionalOnBean，这个注解中有一个@Conditional(OnBeanCondition.class)注解，这个OnBeanCondition配置类，
            继承了SpringBootCondition并实现了getMatchOutcome(context, metadata)钩子方法，所以进入这个类的该钩子
            方法中，进入getMatchingBeans(context, spec);-->getBeanNamesForType();这个方法就是从BeanFactory中获取实例
            如果有则匹配，否是不匹配。
            总结一句话，就是从BeanFactory工厂中匹配是否存在该Bean；
          注解@ConditionalOnClass，这个注解中有一个@Conditional(OnClassCondition.class)，这个OnClassCondition配置类，
             继承了SpringBootCondition并实现了getMatchOutcome(context, metadata)钩子方法，所以进入这个类的该钩子
             方法中，进入filter(onClasses, ClassNameFilter.MISSING, classLoader);
             --classNameFilter.matches(candidate, classLoader)这的classNameFilter因为是ClassNameFilter.MISSING变量，
             所以会进入FilteringSpringBootCondition类的MISSING {}中的 matches()方法-->isPresent(),该方法就是使用
             Class.Name()反射，如果有异常返回false，没有则返回true。
             总结一句话，就是利用反射机制，来判断这个类是否存在。
5.自定义启动器
    当公司里面需要把一些共用的api封装成jar包的时候，就可以尝试自定义启动器来做。
    自定义启动器用到的就是springboot中的SPI原理，springboot会去加载META-INF/spring.factories配置文件。
    加载 EnableAutoConfiguration为key 的所有类。
    利用这一点，我们也可以定义一个工程也会有这个文件。
    1.定义一个核心工程，在resources/META-INF/spring.factories配置如下内容：
        org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
        com.xx.jack.start.CustomStarterRun
        创建CustomStarterRun类，该类会被SpringBoot的SPI加载，这个类里面就可以写一些逻辑，比如可以封装一个开启Redis缓存的
        工具类，用来操作Redis数据库。
    2.自定义Starter
        定义一个没代码的工程，在这个工程里面没有任何代码，只有一个pom 文件。Pom里面就是对前面核心工程jar包的导入。
    3.自定义器的使用
        就只有在springboot工程pom文件里面导入依赖就可以了。这个依赖就是自定义starter哪个工程的maven坐标。
6.Redis自动配置
    在spring-boot-autoconfigure项目下的/META-INF/spring.factories文件下：
    org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration
7.数据源自动配置
    自动配置类
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
    在 springboot 工程中，在不配置数据源对象的情况下，默认是有数据源对象的。默认是有 Hikari 数据源对象的。
8.JdbcTemplate自动配置
    org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration
9.事务管理器自动配置
    org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
10.事务自动配置
    org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
11.DispatcherServlet自动配置
    org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
12.MVC自动配置
    org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration



问题？
1.SpringApplicationRunListener接口是用干嘛的？
    这个是通过SPI机制加载来加载到它的实现类EventPublishingRunListener；
2. this.reader = new AnnotatedBeanDefinitionReader(this);this.scanner = new ClassPathBeanDefinitionScanner(this);
   这个两个类中作用是什么？