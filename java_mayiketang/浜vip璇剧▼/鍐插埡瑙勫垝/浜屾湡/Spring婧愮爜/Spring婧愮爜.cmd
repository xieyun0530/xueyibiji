
Spring分为三个阶段，初始化阶段、注册阶段、刷新上下文阶段。
1.看源码从什么地方看起
    (1)AnnotationConfigApplicationContext，基于注解方式加载spring的配置文件，启动spring容器
    (2)ClassPathXmlApplicationContext，基于xml的方式加载spring的配置文件，启动spring容器
    (3)FileSystemXmlApplicationContext，基于properest文件加载spring的配置文件，启动spring容器
    (4)EmbeddedWebApplicationContext,这个springboot在启动的时候就会用到这个上下文来，
        启动spring容器，new Tomcat 嵌入式tomcat
2.设计模式
    (1)模板设计模式
        父类中定义了一个执行方法的流程，在这个流程方法中的方法延迟到子类来实现，也就是所谓的钩子方法，使用子类的方式来现实
        钩子方法来改变方法的执内容，但是不会改变方法的执行流程，这种设计模式就叫模板设计模式。
        这种设计模式的好处就是，在不改变整个方法的执行流程的情况下，实现不同的功能。
    (2)委托设计模式
        有两个对象参与处理同一个请求，接受请求的对象将请求委托给另一个对象 来处理。在 spring 中用得比较多；也就是这两个类
        会实现同一个接口，然后接受的这个类对象会委托给另一个对象来处理，这种设计模式就叫做，委托设计模式。
    (3)装饰模式
        装饰模式有几个元素很重要，1、被装饰者。2、抽象装饰者。3、装饰者对象
        装饰者：就是需要在原来对象的基础上新增功能的对象
        抽象装饰者：抽象装饰者规定了装饰流程，就是规定了接口调用流程(也就是提供了接口的调用方法)，具体子类实例方法如何调用子类实现，
            (也就是说具体实现了接口的实例方法，交给子类去实现)。
    (4)策略模式
        使用策略模式可以消除大量的if eles，定义一个接口，这个接口定义两个方法，一个方法用来判断实现类是否是处理业务的实现类，
        如果符合返回true，否则返回false；还有一个方法就是实现具体业务逻辑的方法，在主z要的业务代码中，先获取所有实现了这个接口
        的实现类，然后循序的掉哦那个判断是否是处理业务的实现类的方法，找到了就返回该实现类，并调用该实现类的处理业务逻辑的方法；
        例如：根据不同的省份处理不同的业务，可以将每个身份定义成一个接口的实现类，传入不同的身份去循环寻找不同的实现类，这里可以
        使用hasMap去存在所有的省份实例，以提高效率，因为hasMap使用的是hashCode去定位的，在没有hash冲突的情况下，一次就能定位到。
    (5)责任链模式
        使用责任链模式同样也是可以消除大量的if eles，同样也是定义一个接口，但是这个定一个实现业务代码的方法，然后在每一个实现
        类持有一个这个接口的实例，在业务处理方法中使用if eles去判断是否属于这个实现类处理的业务，如果不属于则使用持有的接口实例
        调用业务实现方法，进入下一个业务员处理类，依次去调用就像一个链条一样；
        例如：同样使用省份的例子，根据不同的省份处理不同的业务；首先定义一个接口，这个接口定义一个业务现实方法就行了，然后在每个
        实现类中都持有这个接口的实例，处理业务方法中去判断调用该方法的条件是否是该实现类处理的业务，如果不是则使用接口实例调用
        下一个业务处理实例，形成一条链条去传递，直到满足条件，则就会执行业务方法中的逻辑；这种效率相对于策略模式来说，在调用链
        较长的情况下，效率比较低，所有一般都使用的策略模式去消除if eles。
    (6)建造则模式
        如果需要一次性点给一个对象的属性赋值，则可以使用建造者模式，在每个赋值的方法中返回一个this也就是类的本身实例，这样就可以
        达到使用一句代码给多个属性赋值；
        例如：定义一个类，然后在每个给属性赋值的属性方法中返回一个this，然后可以创建一个对象使用对象不停的点出多个方法，最后
        返回一个给多个属性赋值了的对象实例。
    (7)桥接模式
        在Spring使用JDBC来连接数据库的时候使用到的，有时间了需要去看看这个模式。
3.BeanDefinition中的属性
    (1)id:bean的唯一标识，它必须是合法的 XMLID，在整个 XML 文档中唯一。
    (2)name:用来为id创建别名，它可以是任意的字母符合。多个别名之间用逗号或空格分开。
    (3)class:用来定义类的全限定名（包名＋类名）。只有子类 Bean 不用定义该属性。
    (4)parent:子类bean定义它所引用的父类(包名+类名)，这时前面的class属性失效，子类bean会继承父类bean的所有属性，子类bean也会
       覆盖父类bean的所有属性；注意子类bean和父类bean是同一个java类。
    (5)abstract(默认false):用来定义bean是否为抽象bean，它表示这个bean不会被实例化，一般用于父类bean，因为父类bean主要是提供
      给子类继承使用。
    (6)lazy-init(默认为“default”):用来定义这个Bean是否实现懒初始化。如果为“false”，它将在BeanFactory启动时初始化所有的
       SingletonBean。反之，如果为“true”,它只在Bean请求时才开始创建 SingletonBean。
    (7)autowire（自动装配，默认为“default”）：它定义了 Bean 的自动装载方式。
        1、“no”:不使用自动装配功能。
        2、“byName”:通过Bean的属性名实现自动装配。
        3、“byType”:通过Bean的类型实现自动装配。
        4、“constructor”:类似于byType，但它是用于构造函数的参数的自动组装。
        5、“autodetect”:通过Bean类的反省机制（introspection）决定是使用“constructor” 还是使用“byType”。
    (8)depends-on(依赖对象):这个bean在初始化时依赖的对象，这个对象会在这个Bean初始化之前创建。
    (9)init-method:用来定义bean的初始化方法，它会在bean组装之前调用，它必须是一个无参的方法。
    (10)destroy-method:用来定义Bean的销毁方法，它在BeanFactory关闭时调用，同样，它也必须是一个无参方法，
        它只能用于singletonBean.
    (11)factory-method:定义创建该Bean对象的工厂方法，它用于下面的“factory-bean”，表示这个Bean是通过工厂方法创建的，
        此时Class属性失效。
    (12)factory-bean:定义创建该Bean的工厂类，如果使用了“factory-bean”则“class”属性失效。
    (13)autowire-candidate:采用xml格式创建bean时，将</bean>元素的autowire-candidate属性设置为false，这样容器在自动装配时，
        将不考虑该bean，即它不会被考虑作为其它bean自动装配的候选者，但是该bean本身还是可以使用自动装配来注入其它bean的。
        也就是说如果在其他bean中注入该bean时，这个bean不会注入，也就是说不会创建。
    (14)MutablePropertyValues:用于封装<property>标签信息，其实类里面就是一个list，list里面是PropertyValue对象，
        PropertyValue 就是一个name和value属性，用于封装<property>标签的名称和值信息。
    (15)ConstructorArgumentValues:用于封装<constructor-arg>标签信息，其实里面就是有一个map，map中用构造函数的参数顺序作为
        key，值作为value存储到map中。<constructor-arg>就是用来为构造函数入参用的。
    (16)MethodOverrides:用来封装lookup-method和replaced-method标签的信息，同样的类里面有一个Set对象添加LookupOverrides对象
        和ReplaceOverrides对象。lookup-method用来替换bean中的方法的返回参数的对象；replaced-method用来替换bean中的方法。

3.从ClassPathXmlApplicationContext看起，以下是主要是看一些核心方法
    (1)首先进入ClassPathXmlApplicationContext类中的构造方法中调用了AbstractApplicationContext的refresh()方法初始化环境阶段
        该方法是spring容器初始化的核心方法。是spring容器初始化的核心流程，是一个典型的父类模板设计模式的运用
        根据不同的上下文对象，会调到不同的上下文对象子类方法中
        核心上下文子类有：
        ClassPathXmlApplicationContext
        FileSystemXmlApplicationContext
        AnnotationConfigApplicationContext
        EmbeddedWebApplicationContext(springboot)
    (2)AbstractApplicationContext的refresh()方法下的obtainFreshBeanFactory();该方法的作用是
       obtainFreshBeanFactory();方法的流程：
        1.创建一个BeanFactory工厂，创建工厂的时候，如果这个工厂已存在，则清除BeanFactory和里面的实例
        2.设置是否可以循环依赖，是否允许使用相同名称重新注册不同的Bean实例
        3.解析xml，并把xml的标签封装成BeanDefinition对象
        4.创建xml解析器，这里使用了委托设计模式，将DefaultListableBeanFactory类委托给XmlBeanDefinitionReader
        5.使用流加载xml配置文件，然后封装成Resource对象
        6.EncodedResource带编码的对Resource对象的封装，这里使用模板设计模式，调用子类的实现
        7.获取Resource对象中的xml文件流对象
        8.使用jdk中的InputSource对象Api解析xml文件对象
        9.使用jdk中的api把inputSource封装成Document文件对象
        10.根据document对象拿到xml文件中的标签元素，封装成BeanDefinition
        11.使用委托设计模式，将BeanDefinitionDocumentReader委托这个类进行document的解析
        12.拿到XmlReaderContext上下文，封装XmlBeanDefinitionReader对象
        13.解析Document中的标签
        14.解析默认标签，解析bean标签，创建GenericBeanDefinition对象；
           解析bean标签的属性，并把解析出来的属性设置到GenericBeanDefinition对象中；
           解析完属性后，再解析bean标签的子标签，并把解析出来的子标签设置到GenericBeanDefinition对象中属性中
           比如(MutablePropertyValue对应Property子标签，ConstructorArgumentValuesd对应constructor-arg子标签)；
           解析完了，把GenericBeanDefinition封装到BeanDefinitionHolder对象中，这个对象还封装了bean的id，和bean的别名，
           也就是这个GenericBeanDefinition既有名称也就别名，这样既可以通过名称(id)直接找到GenericBeanDefinition，也可以
           通过别名(别名可以为多个，因为存储的是一个数组)找到对应的名称(id)，再找到对应的GenericBeanDefinition。
        15.解析bean标签中的自定义属性，这里使用了装饰模式，将标签的信息追加到GenericBeanDefinition对象中；
           解析时使用了装饰模式：对BeanDefinitionHolder(这个对象是封装了GenericBeanDefinition)进行装饰，也就是在
           BeanDefinitionHolder对象中新增属性值
           SPI设计思想就是：获取spring中所有jar包里面的"META-INF/spring.handlers"文件，并且建立映射关系
           根据namespaceUri：http://www.springframework.org/schema/p，获取到这个命名空间的处理类
           这里的SPI设计思想，是为了解析bean自定义属性，比如<bean p:=""/>就是为了解析这个中的P标签。
           调用处理类的init方法，在init方法中完成标签元素解析类的注册
        16.解析自定义标签
            使用SPI，解析自定义标签，获取spring中所有jar包里面的"META-INF/spring.handlers"文件，并且建立映射关系，封装到
            一个Map中，key为namespaceUri，value为命名空间的处理类(包名+类名)，然后根据自定义标签的namespaceUri获取要注册
            的标签处理类为命名空间，然后根据这个命名空间使用jdk反射机制进行实例化，调用处理类的init方法，在init方法中完成
            标签元素解析类的注册。
        17.解析<context:component-scan>标签，这个是扫描注解的标签
           (1)根据namespaceUri：http://www.springframework.org/schema/p，获取到这个命名空间的处理类完全限定名(包名+类名)，
              也就是<context:component-scan>标签的处理类。
           (2)所有的标签处理类都继承了NamespaceHandler接口，这里面有两个重要的方法，init()方法，用来实例化标签中的属性对应
              的处理类的，BeanDefinition parse(Element element, ParserContext parserContext)方法，用来将扫描包下的带有
              注解的类，包装成BeanDefinition并且注入到Spring容器中。
           (3)找到<context:component-scan>标签的处理类并实例化以后，使用NamespaceHandler接口接收，NamespaceHandler接口就会
              调用init()方法，该方法就会对属性对应的处理类进行实例化，然后就会返回namespaceUri对应的NamespaceHandler接口的
              实例，NamespaceHandler接口则又会调用BeanDefinition parse(Element element, ParserContext parserContext)方法，
              这个方法里面就会调用<context:component-scan base-package="com.xiangxue.jack"/>，标签的属性component-scan对
              应的处理ComponentScanBeanDefinitionParser()中的parse方法，该方法就是将带有注解的类封装成BeanDefinition注册
              到容器中。
           (3)<context:component-scan>标签component-scan处理类中的parse方法注册带有注解类的BeanDefinition的过程：
              创建一个扫描器，先使用递归的方式将.class文件全部加载到resources数组中，然后验证每个类中是否有includeFilters
              注解，如果有则将这个类包装成BeanDefinition,没有则不做任何操作，将BeanDefinition集合返回，对每个BeanDefinition
              是否支持支持了@Lazy@DependOn等注解，如果有将该BeanDefinition的这些属性值进行设置，然后再将BeanDefinition注册
              到Spring容器中。
           (4)自己顶定义一个自定义标签，是怎么注册的？




Spring源码中为什么要使用委托设计模式？
在不改变源代码的情况下，可以对结果进行修改；假设现在有三个角色分别是：boss、项目经理、员工，boss下发任务到项目经理，
项目经理分配任务给员工，这个时候就是boss将任务委托项目经理，boss只关系任务的结果，而且boss可以下发不同的任务给项目
经理，项目经理根据任务的类型分配给不同的员工。因此委托设计模式，可以将不同的任务委托给委托类，来得到不同的结果，
这样设计的好处是没有改变源码的情况下，可以得到不同的结果，解耦了代码，方便以后的扩展。


1. Spring中Bean的生命周期有哪些步骤？
    (1)使用反射机制实例化bean
2. 什么是BeanDefinition？它为什么非常重要？
    被spring管理的类叫做bean，而BeanDefinition则是用来描述bean的；
3. 什么是Bean的后置处理器？
    用来在初始bean后修，对bean进行操作的方法；
4. 什么是Bean工厂的后置处理器？
    管理bean容器的Bean工厂，可以使用这个后置处理器去修改任何容器中的Bean，而Bean的后置处理器则只能处理本容器的Bean；
5. 什么是BeanFactory？它与ApplicationContext的区别？
    BeanFactory的现实是使用的懒加载的方式，也就是说只有当调用getBean()方法的时候才会去实例化Bean；
    ApplicationContext是spring应用的中央接口，它继承了BeanFactory接口，所以Application包含BeanFactory的所有功能，
    但是ApplicationContext与BeanFactory不同，它采用的是预加载，也就是在ApplicationContext启动后实例化。
    https://www.cnblogs.com/tuanz/p/11124434.html
6. 什么是FactoryBean？它与BeanFactory的区别？
    FactoryBean是一个接口，为IOC中的Bean提供了更加灵活的方式，接口中有一个getObject()方法，这个方法中可以根据
        不同的逻辑动态的返回一个bean，如果是一个FactoryBean的bean实例，当调用getObject('name')返回工厂中的实例,
        当调用getObject('&name')返回工厂本身的实例；所以可以使用FactoryBean去做代理类，在getObject()方法中根据不同
        的class去反射出不同的对象实例；
    BeanFactory是一个用来管理bean的工厂，是ioc容器的顶级接口，是ioc容器最基础的实现，也是访问spring容器的根接口，
        负责对bean的创建，访问等；
    区别：
        BeanFactory是个Factory，也就是IOC容器或对象工厂，FactoryBean是个Bean。在Spring中，所有的Bean都是由BeanFactory
        (也就是IOC容器)来进行管理的。
        对FactoryBean而言，这个Bean不是简单的Bean，而是一个能生产或者修饰对象生成工厂的Bean,它的实现与设计模式中的
        工厂模式和修饰器模式类似。
        Spring使用FactoryBean来做一些Bean的代理，一个Bean来实现FactoryBean接口，在这个实现类中使用反射机制去获取对象，然后
        可以在这个获取对象的前后做一些操作，这样能够在类的前后做一些动作，来实现更多的功能；
    BeanFactory的作用:https://blog.csdn.net/f641385712/article/details/85067006
    Spring源码中AbstractApplicationContext的refresh()方法下FactoryBean的实现：https://blog.csdn.net/xujingzhou/article/details/94674137
7. @Import、@Component、@Bean的区别是什么？
8. 什么是ImportBeanDefinitionRegistrar？它的作用是什么？
















