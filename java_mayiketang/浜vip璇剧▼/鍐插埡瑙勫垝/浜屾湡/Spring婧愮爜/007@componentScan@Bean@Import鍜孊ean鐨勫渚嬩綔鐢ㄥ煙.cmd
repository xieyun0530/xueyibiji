1.Spring中Bean的多例作用域
    (1)单例(singleton)
        在Spring初始话的时候已经将其实例化后，将实例存在了缓存中了。
    (2)多例(scope)
        与单例最大的区别是，spring初始化的时候没有初始化，且调用getBean方法去实例化的时候，每次获取的都是一个新的
        实例，因为多例创建完后不会进行缓存，所以每次创建的都是不同的实例。
        创建实例的时候，会先将beanName放到一个正在创建beanName缓存中(currenty)
    (3)request作用域
        使用了ThreadLock隔离一个线程各自拥有自己的实例
    (4)session作用域
        这个是接口tomcat来做
2.@componentScan@Bean@Import注解实现
    (1)@componentScan
        其实这个跟xml的自定义标签扫描器<context:component-scan>类似，只是注解是通过判断类中是否带有@componentScan的注解，
        如果有则获取注解上的包名地址(包名可以有多个使用逗号隔开)，通过包名使用递归的方式读取报下所有的文件，然后循环判断
        每个类中是否含有@component注解，把含有@component注解的类包装成beanDefinition。
    (2)@Bean
        Spring的@Bean注解用于告诉方法，产生一个Bean对象，然后这个Bean对象交给Spring管理。产生这个Bean对象的方法Spring只
        会调用一次，随后这个Spring将会将这个Bean对象放在自己的IOC容器中。
    (3)@Import
        @Import只能用在类上 ，@Import通过快速导入的方式实现把实例加入spring的IOC容器中
        @Import三种将类加入ioc的方式：
        普通使用方法：
            @Configuration
            @Import(value={UserServiceImpl.class})
            public class Config {
            }
            但是这种方式有一些问题，那就是只能使用类的无参构造方法来创建bean，对于有参数的构造方法就无能为力了
        结合ImportBeanDefinitionRegistrar接口：
            ImportBeanDefinitionRegistrar接口的源码如下：
            public interface ImportBeanDefinitionRegistrar {
                public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry);
            }
            可以看到这个接口唯一的方法是有两个参数的
            AnnotationMetadata：通过这个参数可以拿到类的元数据信息
            BeanDefinitionRegistry：通过这个参数可以操作IOC容器
            我们可以使用一个类来实现这个接口
            public class UserServiceBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {           
                public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,BeanDefinitionRegistry registry) {
                    BeanDefinitionBuilder userService = BeanDefinitionBuilder.rootBeanDefinition(UserServiceImpl.class);
                    registry.registerBeanDefinition("userService", userService.getBeanDefinition());
                }             
            }
            可以看到我们在这个方法里面做一些特殊操作什么的都是可以的，相比较于普通的方式可是灵活了很多
            接着我们在@Import注解引入的地方只需要修改为引入UserServiceBeanDefinitionRegistrar就可以了
            @Configuration
            @Import(value={UserServiceBeanDefinitionRegistrar.class})
            public class Config {
            }
        结合ImportSelector接口：
            相比较与实现ImportBeanDefinitionRegistrar接口之后直接操作Bean容器来说，使用ImportSelector会更加优雅一些，
            只需要返回需要注入类的全限定名就可以了
            ImportSelector接口的源码如下：
            public interface ImportSelector {
                String[] selectImports(AnnotationMetadata importingClassMetadata);
            }
            public class UserServiceImportSelect implements ImportSelector{            
                public String[] selectImports(AnnotationMetadata importingClassMetadata) {
                   return new String[]{UserServiceImpl.class.getName()};
                }
            }
            @Configuration()
            @Import(value={UserServiceImportSelect.class})
            public class Config {
            }
            相比较三种方式来说可以看到最后这种才是最优雅的方式



















