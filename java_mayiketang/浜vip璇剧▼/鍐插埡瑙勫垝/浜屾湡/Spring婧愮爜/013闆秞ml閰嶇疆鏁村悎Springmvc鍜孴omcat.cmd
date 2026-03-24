1.SpringMVC注解方法启动的原理
    https://my.oschina.net/u/3995125/blog/3081947
    https://msd.misuland.com/pd/2884250171976191532
    (1)在web应用启动的时候动态添加servlet、filter和listener
        基于spi机制，META-INF/services/javax.servlet.ServletContainerInitializer文件中存放实现该接口的类，
        这些类会被容器调用；
    (2)进入：org.springframework.web.SpringServletContainerInitializer该类中有@HandlesTypes(WebApplicationInitializer.class)的注解
        SpringServletContainerInitializer为spring中实现ServletContainerInitializer接口的唯一类，该类主要是从容器
        获取实现@HandlesTypes(WebApplicationInitializer.class)注解中的WebApplicationInitializer接口的实现类，并且按照
        次序（javax.annotation.Priority）调用其onStartup方法。
    (3)servlet容器启动的时候会加载org.springframework.web.SpringServletContainerInitializer这个类，调用初始化类的onStartup方法,
        SpringServletContainerInitializer这个类间接的实现了WebApplicationInitializer接口的onStartup方法；
    (4)SpringServletContainerInitializer类的onStartup方法会反射实例化AbstractDispatcherServletInitializer类，并调用它的onStartup
        方法；进入到DispatcherServlet初始化器org.springframework.web.servlet.support.AbstractDispatcherServletInitializer类的
       onStartup()这个方法，这个方法中有两个方法super.onStartup(servletContext)；registerDispatcherServlet(servletContext);
    (5)super.onStartup(servletContext)；这个方法先去获取上下文对象，再去创建一个监听器ContextLoaderListener把获取到的上下文
       对象放入到监听器中，最后把监听器放入到servlet容器中；
    (6)这里再创建监听器的方法的时候，会提供了一个getRootConfigClasses()的钩子方法，从子类实现中或者从@ComponentScan注解配置中
        获取配置类；这个钩子方法可以通过继承AbstractAnnotationConfigDispatcherServletInitializer抽象类来实现这个钩子方法，
        其实就是一个模板设计模式；如果没有配置就默认初始化基于注解的web应用上下文类；
    (7)registerDispatcherServlet(servletContext);这个方法其实也是创建一个上下对象，再创建一个DispatcherServlet
       FrameworkServlet(这个类是DispatcherServlet的父类)对象，将DispatcherServlet同样也放入到servlet容器中；
       这里实现其实跟监听器的实现是一样的；
    (8)总结：监听器和Dispatcher这个两个是父子容器的关系，监听器是父容器，Dispatcher是子容器；
2.取代web.xml配置
    (1)加载Listener监听器，使用的是ContextLoaderListener这个类，这个类是在
        AbstractAnnotationConfigDispatcherServletInitializer--》AbstractDispatcherServletInitializer的
        onStartup(servletContext)方法的第一个super.onStartup(servletContext)父类AbstractContextLoaderInitializer的
        onStartup(servletContext)方法的registerContextLoaderListener(servletContext)方法中，这里会创建一个
        AnnotationConfigWebApplicationContext的上下文，且注册了一个带有@ComponentScan扫描器的注解类，这个注解的获取是在
        AbstractAnnotationConfigDispatcherServletInitializer类的createServletApplicationContext()方法中，
        这个方法里面有一个getRootConfigClasses()的钩子方法，这里我们使用了去继承
        AbstractAnnotationConfigDispatcherServletInitializer类来实现一个钩子方法，这里返回一个带有@ComponentScan
        扫描器的注解类的数组就行了，因为可以有多个；
        将@ComponentScan扫描器的注解类的上下文对象，放入到ContextLoaderListener监听器对象中去；
    (2)加载DispatcherServlet，使用的是FrameworkServlet这个类，这个类是在
        AbstractAnnotationConfigDispatcherServletInitializer--》AbstractDispatcherServletInitializer的
        onStartup()方法的第二个方法registerDispatcherServlet(servletContext)在调用的父类
        AbstractDispatcherServletInitializer的registerDispatcherServlet(servletContext)方法，这个方法同样
        也创建了一个springmvc的上下文，且注册了一个带有@ComponentScan扫描器的注解类，这个注解的获取是在
        AbstractAnnotationConfigDispatcherServletInitializer类的createServletApplicationContext()方法中，
        这个方法里面有一个getServletConfigClasses()的钩子方法，这里我们使用了去继承
        AbstractAnnotationConfigDispatcherServletInitializer类来实现一个钩子方法，这里返回一个带有@ComponentScan
        扫描器的注解类的数组就行了，因为可以有多个；将@ComponentScan扫描器的注解类的上下文对象，
        放入到FrameworkServlet监听器对象中去；
    (3)Listener监听器的启动：(就是启动spring容器)
        在ContextLoaderListener类中执行contextInitialized(ServletContextEvent event)方法，该方法会调用父类的
        initWebApplicationContext(ServletContext servletContext)方法，这个方法会拿到上下文对象调用refresh方法，
        需要特殊记忆的就是，会把上下文对象设置到servletContext 中。
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
    (4)DispatcherServlet的启动：(就是启动spring容器)
        执行FrameworkServlet的父类HttpServletBean类中的init()方法，会从servletContext中获取父容器，获取父容器的方法
        链initServletBean();--》FrameworkServlet.initServletBean()-->initWebApplicationContext()
        -->WebApplicationContextUtils.getWebApplicationContext(getServletContext())
        --> getWebApplicationContext(ServletContext sc, String attrName),
        就是由listener负责启动的容器，然后把父容器设置到了自己的上下文对象中，所以这里监听器启动的容器是父容器，
        dispatcherServlet启动的容器是子容器，两者是父子关系。这里就用 servlet规范完成了取代web.xml的工作，并启动了容器。
3.请求之前建立映射关系
    (1)装配注解@EnableWebMvc，这个注解中有一个@Import(DelegatingWebMvcConfiguration.class)注解，再进入
       DelegatingWebMvcConfiguration的父类WebMvcConfigurationSupport类中，这个类中是具体注入很多bean这里先讲解
       一个RequestMappingHandlerMapping类，在启动容器的时候会执行一个RequestMappingHandlerMapping类的父类
       RequestMappingInfoHandlerMapping类，再到父类AbstractHandlerMethodMapping这个类，会执行这个类的
       afterPropertiesSet()方法；
    (2)这个类的主要作用就是是如下步骤：
        1、循环类里面的所有方法
        2、收集方法上面的@RequestMapping注解，把注解里面的配置信息封装到类里面，该类就是 RequestMappingInfo类，
           并且跟类上面的@RequestMapping 注解封装类RequestMappingInfo 合并，比如类上面是/common，方法上面
           是/queryUser。这两者合并后就是/common/queryUser。这样的 url 才是我们需要的。合并完就是这样的url
        3、然后建立method对象和对应的RequestMappingInfo 的映射关系，把关系存放到 map 中。
        4、创建 HandlerMethod 对象，该类型封装了 method、beanName、bean、方法类型等 信息
        5、建立RequestMappingInfo和HandlerMethod的映射关系
        6、形成一个映射关系表保存在一个RequestMappingHandlerMapping bean中
        7、然后在客户请求到达时，再使用RequestMappingHandlerMapping中的该映射关系表找到相应的控制器方法去处理该请求
    (3)RequestMappingHandlerMapping中保存的每个"<请求匹配条件,控制器方法>"映射关系，请求匹配条件
       通过RequestMappingInfo包装和表示，而"控制器方法"则通过HandlerMethod来包装和表示。
       HandlerMethod对象，该类型封装了 method、beanName、bean、方法类型等信息
4.dispatcherServlet处理请求
    (1)当请求过来时，首先会调用到dispatcherServlet中的doService方法，最终会调用到dispatcherServlet中的doDispatch()方法;
    (2)根据请求url获取HandlerExecutionChain对象，这个对象是在getHandler(processedRequest)方法中获取到的；
    (3)getHandler(processedRequest)方法中有一个getHandlerInternal(request)方法，这个方法是从request对象
       中获取uri，根据uri从映射关系中找到对应的HandlerMethod对象，获取过程是，先从urlLookup中获取
       RequestMappingInfo对象，然后再根据RequestMappingInfo对象从mappingLookup中获取HandlerMethod对象；
    (4)获取到HandlerMethod对象后，把HandlerMethod对象封装到HandlerExecutionChain对象中了；这个对象，
       启动就是封装了HandlerMethod和一个拦截器数组而已。
    (5)再回到doDispatch()方法，接下来到前置过滤器mappedHandler.applyPreHandle(processedRequest, response)；
    (6)拿到 HandlerExecutionChain 对象进行过滤器的调用，调用了preHandle方法，只要这个方法返回为false，
       则后续请求就不会继续，也就是如果请求在这个前置的过滤器的范围内返回false，该请求就会被过滤掉，不会再
       往下执行了；
    (7)没有被过滤掉，就会走到ha.handle(processedRequest, response, mappedHandler.getHandler())方法，这个方法
       就是HandlerAdapter调用handle方法，进行具体Controller中方法的调用这个调用过程，关键点就在于参数的解析；
    (8)进入handle方法中的步骤，AbstractHandlerMethodAdapter.handle()-->RequestMappingHandlerAdapter.handleInternal()
       -->invokeHandlerMethod()-->ServletInvocableHandlerMethod.invokeAndHandle()
       -->InvocableHandlerMethod.invokeForRequest()-->getMethodArgumentValues()这个方法就会获取到一个参数数组，
       然后根据参数类型，获取不同的参数解析器去解析不同的参数，这里使用了一个策略模式；
    (9)首先获取方法的参数列表，并且把参数封装成MethodParameter对象，这个对象记录了参数在参数列表中的索引，
       参数类型，参数上面的注解数组等等信息。然后循环参数列表，一个个参数来处理，这里是一个典型的策略模式的运用，
       根据参数获取一个处理该参数的类。
    (10)把参数一个个处理完成后，放到一个参数数组中了Object[]args接下来就是反射调用了，有方法method 对象，
        有类对象，有参数数组就可以进行反射调用了，这里进行反射调用是在InvocableHandlerMethod.invokeForRequest()
        方法下的doInvoke(args)进行反射调用的，args这个是一个数组，也就是多个参数，这个参数是使用的MethodParameter
        类进行了包装里面包装了参数类型，参数名称，参数注解等等信息，所有可以进行反射调用。
5.HandlerMapping和HandlerAdapter的注册
    (1)HandlerMapping(根据http请求的url映射到controller的方法中)
       用来解析controller类或者其他处理器的，不仅限于解析解析controller，主要职责是将HTTP请求映射到相应的处理器Handler；
       HandlerMapping的注册是使用@Bean方式注册进来的，这个类在容器启动的时候会将类上面有@Controller注解
       或者@RequestMapping注解的,建立uri和method的映射关系urlLookup的Map容器，建立RequestMappingInfo和HandlerMethod的
       映射关系mappingLookup的Map容器，这样就可以通过一个url找到唯一的一个HandlerMethod；当然这里还注入了拦截器，拦截器
       同样也提供了一个钩子方法，给子类实现，所有我们就可以继承父类WebMvcConfigurationSupport的方式去把拦截器添加到容器中,
       还有处理跨域请求的钩子方法，这个后面可以研究下；
       总结：
           1.HandlerMapping的核心作用是根据HTTP请求（如 URL、方法）找到对应的Controller类和方法；
           2.它在启动阶段解析所有的映射关系，运行时根据请求快速品匹配；
           3.RequestMappingHandlerMapping是最常用的实现，用于解析基于注解的映射关系；
    (2)HandlerAdapter适配器
        HandlerAdapter也是使用@Bean的方法注入到容器中来的，这个适配器是负责根据不同的请求来适配不同的处理器，去处理
        不同的请求；
        从源码分析看来，发现处理器根本就不只有Controller这一种。还有HttpRequestHandler，Servlet等处理器。
        下面来介绍一下几种适配器对应的处理器以及这些处理器的作用：
        1.AnnotationMethodHandlerAdapter主要是适配注解类处理器，注解类处理器就是我们经常使用的@Controller的这类处理器
        2.HttpRequestHandlerAdapter主要是适配静态资源处理器，静态资源处理器就是实现了HttpRequestHandler接口的处理器，
           这类处理器的作用是处理通过SpringMVC来访问的静态资源的请求。
        3.SimpleControllerHandlerAdapter是Controller处理适配器，适配实现了Controller接口或Controller接口子类的处理器，
          比如我们经常自己写的Controller来继承MultiActionController.
        4.SimpleServletHandlerAdapter是Servlet处理适配器,适配实现了Servlet接口或Servlet的子类的处理器，我们不仅可以
          在web.xml里面配置Servlet，其实也可以用SpringMVC来配置Servlet，不过这个适配器很少用到，而且SpringMVC默认的
          适配器没有他，默认的是前面的三种。
        5.总结：
            1.执行由HandlerMapper确定的处理器逻辑；
            2.不负责controller方法的解析，而是负责controller方法的调用，并处理参数绑定和返回值；
            3.与controller的关系是，HandlerAdapter不直接解析Controller类或者方法，但是会调用它们完成逻辑处理；
            4.常见的HandlerAdapter实现类
                实现类	                       适配的处理器类型
                HttpRequestHandlerAdapter	    HttpRequestHandler接口
                SimpleControllerHandlerAdapter	实现了Controller接口的处理器
                RequestMappingHandlerAdapter	带有@RequestMapping的方法
6.SpringMVC请求的源码流程解析
    (1)根据请求的request对象获取到handlerMapping对象
    (2)根据请求类型获取到不同的HandlerAdapter，这里使用了策略模式来消除if eles；
    (3)调用前置处理器preHandle，可以做一些权限验证，这列如果前置过滤器方法返回false，先调用后置处理器，然后再返回；
       这个而且是倒序的方式调用后置处理器，因为后置处理器是用来释放资源的；
    (4)调用ha.handle处理器，处理Controller中具体方法调用，这里面会使用参数解析器，根据不同的注解去解析不同的参数；
       这个获取参数使用了策略设计模式，先拿到所有的入参处理类，然后根据入参的注解类型循环去匹配对应的参数解析器，
       获取到这些参数后，使用反射的方式调用实例方法，调用完成后，根据返回参数的注解返回不同的返回参数格式，这里使用
       了模板方法，去获取不同的返参格式；
    (5)调用中置处理器applyPostHandle--》postHandle，这里一样修改mv也就是返参；
    (6)视图渲染processDispatchResult--》render
    (7)调用后置处理器triggerAfterCompletion--》afterCompletion；这个主要是做资源释放的；
7.filter过滤器
    这个过滤器不是Spring来管理的，而是通过tomcat来调用的，这个是在调用springMVC之前的；
8.ha.handle()方法
    (1)调用有@ModelAttribute注解的方法
        当调用@Controller类中的方法时，每次请求都会调用有@ModelAttribute注解的方法,把@ModelAttribute注解的方法的返回值
        存储到 ModelAndViewContainer对象的map中了,解析@RequestMapping的方法时，会将这个map带入到解析方法中，
        @RequestMapping方法中的带有@ModelAttribute参数的值，就是通过带入进来的ModelAndViewContainer对象的map中获取到的。
9.解决跨域问题
    (1)产生跨域问题的是A服务调用B服务产生的跨域问题，这种问题是浏览器的原因产生的
    (2)解决跨域问题的方法：
        1.使用Servlet的过滤器Filter实现，在doFilter方法中，把response相应参数中加一个"Access-Control-Allow-Origin", "*"的
        参数去实现，代码如下：
            HttpServletResponse response = (HttpServletResponse) res;
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        2.使用SpringMVC的过滤器实现，通过显示WebMvcConfigurationSupport类中的addCorsMappings()钩子方法来实现，在这个钩子
          方法中同样修改响应头添加Access-Control-Allow-Origin信息等；同时会建立url和CorsConfiguration对应关系的容器，在
          SpringMVC注解方法启动的原理请求的时候，判断请求头如果有Origin属性，这就是会容器中去对应的实例，从而修改请求头
          达到实现跨域请求的目的；
        3.在controller在类上标了@CrossOrigin或在方法上标了@CrossOrigin注解，则spring在记录mapper映射时会记录对应跨域请求映射，
        4.第2种和第三种的实现都是在SpringMVC的请求过程中实现的，在getHandler()获取HandlerMethod的方法中，有一个判断就是
          查看request请求头中是否有Origin属性，如果有则去拿到跨域的全局配置，拿到handler的跨域配置，处理跨域（即往响应头添加
          Access-Control-Allow-Origin信息等），并返回对应的handler对象，最终值执行器添加一个连接器链
10.有时间可以研究下Spring注解事务中，多个事务使用不同的数据源，传播属性都用默认的，执行逻辑是怎样的？




















