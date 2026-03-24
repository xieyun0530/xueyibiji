1.java spi机制
    (1)SPI全称Service Provider Interface，是Java提供的一套用来被第三方实现或者扩展的API，它可以用来启用框架扩展和替换组件。
    (2)就是使用者只要使用定义的接口，而服务者去实现这些接口，将实现打成jar包的形式给消费者使用，这样如果服务者需要修改接口
        的实现的话，消费者完全不需要修改任何代码，只需要服务者将修改后的代码，重新打一个jar给消费者引用就行了，实现了解耦。
    (3)常见的例子有：
        数据库驱动加载接口实现类的加载，JDBC加载不同类型数据库的驱动；
        日志门面接口实现类加载，SLF4J 加载不同提供商的日志实现类；
        Spring 中大量使用了 SPI,比如：
            对servlet3.0规范对ServletContainerInitializer的实现、自动类型转换
            Type Conversion SPI(Converter SPI、Formatter SPI)等。
2.java spi的约定
    (1)当服务提供者提供了接口的一种具体实现后，在jar包的META-INF/services目录下创建一个以“接口全限定名”为命名的文件，
       内容为实现类的全限定名；
    (2)接口实现类所在的jar包放在主程序的classpath中；
    (3)主程序通过java.util.ServiceLoder动态装载实现模块，它通过扫描META-INF/services目录下的配置文件找到实现类的全限定名，
       把类加载到JVM；
    (4)SPI的实现类必须携带一个不带参数的构造方法；
3.Dubbo SPI机制
    java spi 机制非常简单，就是读取指定的配置文 件，将所有的类都加载到程序中。而这种机制，存在很多缺陷，比如：
    1.所有实现类无论是否使用，直接被加载，可能存在浪费
    2.不能够灵活控制什么时候什么时机，匹配什么实现，功能太弱 Dubbo 基于自己的需要，增强了这套 SPI 机制。
4.Dubo的SPI的三种用法
    传统的用法：
        全部将实现类加载ServiceLoader<InfoService> serviceLoader = ServiceLoader.load(InfoService.class);
        serviceLoader是实现了Iterable的迭代器，直接遍历实现类；
    (1)标签@SPI 用法
        在接口上加上@SPI的注解；
        使用ExtensionLoader.getExtensionLoader(InfoService.class);
        extensionLoader.getExtension("a");获取别名为"a"的实现类的实例；
        InfoService infoServiceB = extensionLoader.getDefaultExtension();获取默认的实现类@SPI("b")这个"b"就表示默认的;
    (2)标签@Activate用法
        当我们需要在一批实现类中找到多个实现类的时候，就需要使用这个@Activate注解的方式，这个是用了一个过滤器的思维去做的，
        Activate 注解表示一个扩展是否被激活(使用),可以放在类定义和方法上，dubbo 用它在 spi 扩展类定义上，表示 这个扩展
        实现激活条件和时机。它有两个设置过滤条件的字段，group，value 都是字符数组。 用来指定这个扩展类在 什么条件下激活。
        //表示如果过滤器使用方（通过 group 指定）属于 Constants.PROVIDER（服务提供方）
          或者 Constants.CONSUMER（服务消费方）就激活使用这个过滤器
        @Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
        public class testActivate1 implements Filter {
        }
        //表示如果过滤器使用方（通过 group 指定）属于 Constants.PROVIDER（服务提供方）
            并且 URL 中有参数 Constants.TOKEN_KEY（token）时就 激活使用这个过滤器；order表示排序
         @Activate(group = Constants.PROVIDER, value = Constants.TOKEN_KEY,order = 1,value = "oooooo")
         public class testActivate2 implements Filter { }
        实现：
            首先实现类的接口都去实现Filter过滤器这个接口；
            调用的时候使用ExtensionLoader.getExtensionLoader(Filter.class);类加载器加载实现类；
             ExtensionLoader extensionLoader = ExtensionLoader.getExtensionLoader(Filter.class);
             URL url = URL.valueOf("test://localhost/dsafsdfsdfsd");
             url = url.addParameter("oooooo", "66666");
             url = url.addParameter("groupId", "qqqq");
             url = url.addParameter("myfilter", "a,-c");
             List<Filter> list = extensionLoader.getActivateExtension(url,"myfilter"(key), "peter"(group));
             list：就是获取的所有符合条件的实现类；
            url(URL.valueOf("")):表示一个请求协议；
                url.addParameter("oooooo", "66666");这个表示找到@Activate注解中的value等于"oooooo"的Filter的实现类；
                这里的value值可以随便写，在实现类的invoke方法中的invocation参数能获取到；
            key(url.addParameter("myfilter", "a,-c"))：使用url.addParameter("myfilter", "a,-c")
                这里的"a"表示添加一个"a"别名的实现类进来，"-c"则表示删除别名为"c"实现类；
            group(url.addParameter("groupId", "qqqq");)：则是找到@Activate中的group值为"peter"的是实现类；
    (3)标签@Adaptive用法
       @Activate标签不能在程序运行时动态指定，就是 extensionLoader.getExtension 方法写 死了扩展点对应的实现类，
       不能在程序运行期间根据运行时参数进行动态改变。而有我们如果需要对，实现类进行懒加载，dubbo使用了@Adaptive注解
       来实现一个懒加载。
       @Adaptive注解可以加在方法上，也可以加在类上，如果是加在方法上则只能调用带有@Adaptive注解的方法，因为静态代理
       生产代理类的时候只会在带有@Adaptive注解的方法上有实现，而没有注解的则会是一个抛出异常的空方法，
       如果是类则会生产类下面的所有方法的代理；
       实现：
            ExtensionLoader<InfoService> loader = ExtensionLoader.getExtensionLoader(InfoService.class);
            //这里其实会返回一个返回一个代理类的实例，代理类中则会根据参数调用具体的实例类
            InfoService adaptiveExtension = InfoService adaptiveExtension = loader.getAdaptiveExtension();
            //info.service=a 的参数名格式，是接口类InfoService的驼峰大小写拆分，
                "a"表示调用@Adaptive注解的别名为"a"的是实现类
            URL url = URL.valueOf("test://localhost/test?info.service=a");
            //URL中有具体的值InfoService=c，则以URL中的InfoService参数为准，选择C实现
            URL url = URL.valueOf("test://localhost/test?info.service=a&InfoService=c");
       @Adaptive({"mark"})中的mark值，表示URL.valueOf("test://localhost/test?mark=c")适配url地址中的mark变量的值，也就是
       取地址中mark的值。
5.javassist动态编译
    在SPI寻找实现类的过程中，getAdaptiveExtension方法得到的对象，只是个接口代理对象，此代理对象是由临时编译的类来实现的。
    在此，先说明一个 javassist 动态编译 类的两种用法：
    通过创建 class 模型对象设置 class 属性，然后生成 Class：
    1. CtClass-->CtField-->CtMethod
    2. Class<?> clazz = ctClass.toClass()
    直接编译拼凑好的定义 class 的字符串，来生成 class：
    JavassistCompiler.compile("public class DemoImpl implements DemoService {...}",ClassLoader());
5.Dubbo SPI 的依赖注入
    Dubbo SPI 的核心实现类为 ExtensionLoader，此类的使用几乎遍及 Dubbo 的整个源 码体系。是大家以传统方式读源码的严重障碍。
    ExtensionLoader 有三个重要的入口方法，分别与@SPI、@Activate、@Adaptive注解对应。
    getExtension 方法，对应加载所有的实现
    getActivateExtension 方法，对应解析加载@Activate 注解对应的实现
    getAdaptiveExtension 方法，对应解析加载@Adaptive 注解对应的实现
    其中，@Adaptive 注解作的自适应功能，还涉及到了代理对象（而Dubbo的代理机制，有两种选择，jdk 动态代理和javassist动态编译类）。
    我们将后后续篇章对此进行说明。














