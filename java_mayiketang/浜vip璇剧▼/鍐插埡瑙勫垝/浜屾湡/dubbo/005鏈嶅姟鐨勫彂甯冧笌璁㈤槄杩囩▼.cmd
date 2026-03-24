1.rpc过程
    (1)在网络传输的过程中，是由protocol协议组件来完成的，无论是什么协议都是将消费端的调用信息(interface接口描述)，
       传递到服务端进行java反射调用；
    (2)服务端使用protocol协议来监听网络。当数据来临时，需要按信息指示(interface接口描述)，调用对应的service服务来响应；
    (3)消费端发起远程调用，它需要将本地代理对象动作，转换成调用信息（interface 描述），通过protocol 发送到网络上；
    (4)整个过程自始至终涉及到的信息有两种，一种为url 信息（协议 头://ip:port/path）,一种 interface 参数。
       为了简化三个部分的协作，Dubbo 提出一个实体域对象 invoker（内部封装了 url 和 interface）
    (5)invoker的思想：万事万物只有一个调用入口，invoker 的 invoke 方法。因此，服务端 protocol 对本地
       service的调用，被封装成了 invoker;消费端发起远程调用到网络的动作，也被封装成了invoker。
    (6)从此，服务端监听到的网络请求，将自动触发服务端绑定 invoker.invoke, 消费端直接调用消费端的
       invoker.invoke 也自动将信息发送到了protocol网络上。
    总结：
        就是将服务端的反射代码放入到一个invoke代理对象中进行反射调用，再将这个代理对象放入protocol协议中进行暴露，
        这样服务端的protocol中直接使用这个代理对象直接调用invoke.invoke方法，而不去直接拿到反射后的对象去调用方法；
        而消费端同样也是将消费端的远程调用封装到一个invoke.invoke方法，然后将这个invoke对象做了一个静态代理，这样
        消费端就直接去调用这个代理方法，这样消费端的调用就像调用本地方法一样去的调用这个代理方法，其实这个代理方法
        真实的调用是invoke.invoke方法，这样无论是服务端还是消费端都是统一的一个invoke入口方法，统一了入口；
        使用者可以对这个invoke进行扩展，而protocol协议只需要调用invoke.invoke方法就行了，这样的设计思想就是可以
        不改变整理流程代码，而我们可以去不改变通讯协议代码的情况下，对这个invoke进行扩展一些其它功能。
2.dubbo的服务注解与发现机制
    (1)protocol协议中服务暴露和注册方法
        dubbo是通过protocol.export(serviceInvoker)将服务暴露出去的；
        protocol.refer(DemoService.class, registryUrl)将网络上的服务引入进来的；
    (2)RegistryProtocol服务发现和注册的包装类
        protocol对象本身就是一个SPI适配对象，它可以根据不同的url选择不同的protocol实现类，于是dubbo在此基础上，进行了
        一层嵌套：将服务的注册/发现功能，抽象成了一个protocol协议，即RegistryProtocol（并不是个真的协议实现，是来做
        服务发现注册使用）, 并在 RegistryProtocol 对象内，再次嵌套一个真实的 protocol；
        也就是RegistryProtocol内包含了protocol协议的调用，将protocol协议包括在了RegistryProtocol中；
        在RegistryProtocol内存去调用protocol协议去实现服务的暴露和注册；
    (3)服务暴露是注册服务
        进入ServiceBean.afterPropertiesSet()--》ServiceBean.export()--》ServiceConfig.export()-->ServiceConfig.doExport()
        -->ServiceConfig.doExportUrls();
        而 doExportUrls 方法分成两部分:
        // 1.获取注册中心URL,将所有服务URL封装为List，得到服务提供者集合registryURLs
        URL List<URL> registryURLs = loadRegistries(true);
        // 2.遍历所有协议，export protocol 并注册到所有注册中心
        for (ProtocolConfig protocolConfig : protocols) {
            doExportUrlsFor1Protocol(protocolConfig, registryURLs);
        }
        其中，loadRegistries 获取配置的注册中心 URL 首先执行 checkRegistry，判断是否有配置注册中心，如果没有，则从默认配
        置文件dubbo.properties中读取dubbo.registry.address组装成RegistryConfig。
        然后根据 RegistryConfig 的配置，组装 registryURL，形成的 URL 格式如下:
        registry://127.0.0.1:2181/com.alibaba.dubbo.registry.RegistryService?application=demo-provider&registry=zookeeper
        这个URL表示它是一个registry协议(RegistryProtocol)，地址是注册中心的 ip:port，服务接口是 RegistryService，
        registry的类型为zookeeper。
        doExportUrlsFor1Protocol 发布服务和注册因为 dubbo 支持多协议配置，对于 每个 ProtocolConfig 配置，组装 protocolURL，
        注册到每个注册中心上。
        doExportUrlsFor1Protocol方法：
            1.String host = this.findConfigedHosts(protocolConfig, registryURLs, map);用来获取绑定的ip
            2.Integer port = this.findConfigedPorts(protocolConfig, name, map);用来获取绑定的端口
            3.URL url = new URL(name, host, port, (contextPath == null || contextPath.length() == 0 ? "" : contextPath + "/")
              + path, map);构建一个url对象，也就是创建 protocol export url
            4.构建出的 protocolURL 格式如下:
              dubbo://192.168.199.180:20880/com.alibaba.dubbo.demo.DemoService?anyhost=true&application=demo-provider&
              bind.ip=192.168.199.180&bind.port=20880&dubbo=2.0.0&generic=false&interface=com.alibaba.dubbo.demo.DemoService&
              methods=sayHello&pid=5744&qos.port=22222&side=provider&timestamp=1530746052546
              这个 URL 表示它是一个 dubbo 协议(DubboProtocol)，地址是当前服务器的 ip，端口是要暴露的服务的端口号，可以从
              dubbo:protocol 配置，服务接口为 dubbo:service 配置发布的接口。
            5.遍历所有的registryURL(服务提供者集合),以 registryUrl 创建 Invoker，将Invoker和ServiceConfig包装成
              DelegateProviderMetaDataInvoker，以RegistryProtocol为主，注册和订阅注册中心，并暴露本地服务端口；
              也就调用protocol.export(wrapperInvoker);将DelegateProviderMetaDataInvoker暴露出去；
            6.由此进入RegistryProtocol.export()-->RegistryProtocol.doLocalExport()这个方法就是调用protocol协议进行invoke的
              暴露，这个protocol是通过SIP实现了protocol接口上的@SPI("dubbo")注解默认的是dubbo协议，当然我们也可以通过在url的
              添加参数的方式去指定一个协议，调用protocol.export(invokerDelegete)暴露服务后，会将export的返回值Exporter对象
              包装成ExporterChangeableWrapper对象进行缓存，这个ExporterChangeableWrapper继承了Exporter接口。
              这里Exporter的接口对象是用来存储什么？
    (4)服务引入时订阅服务地址
        ReferenceBean.afterPropertiesSet()-->ReferenceBean.getObject()-->ReferenceConfig.get()-->ReferenceConfig.init()方法,
        最后会来创建代理对象 ref = createProxy(map);这个方法主要用来找到并校验配置的 urls，此 url 一般 只是一个注册中心的 url。
        值类似下面这样：registry://127.0.0.1:2181/com.alibaba.dubbo.registry.RegistryService?registry=zookeeper，
        所以当refprotocol.refer调用时，直接还是适配到RegistryProtocol。 RegistryProtocol的refer动作，会转发到doRefer方法，也
        就是createProxy(map)-->RegistryProtocol.refer()--doRefer()，这个方法里面就是组装一个url订阅服务。
3.注册与订阅
    dubbo 的注册/订阅动作，主要涉及以下接口：
        org.apache.dubbo.registry.Registry其中Registry接口继承自RegistryService，负责注册/订阅动作，RegistryService主要方法：
        /*** 注册服务.
            * @param url 注册信息，不允许为空，
            如： dubbo://10.20.153.10/com.alibaba.foo.BarService?version=1.0.0&application=kylin
        */
        void register(URL url);
        /*** 取消注册服务.
            * @param url 注册信息，不允许为空，
            如： dubbo://10.20.153.10/com.alibaba.foo.BarService?version=1.0.0&application=kylin
        */
        void unregister(URL url);
        /*** 订阅服务. *
            * @param url 订阅条件，不允许为空，
            如： consumer://10.20.153.10/com.alibaba.foo.BarService?version=1.0.0&application=kylin
            * @param listener 变更事件监听器，不允许为空
        */
        void subscribe(URL url, NotifyListener listener);
        /*** 取消订阅服务.
            * @param url 订阅条件，不允许为空，
            如： consumer://10.20.153.10/com.alibaba.foo.BarService?version=1.0.0&application=kylin
            * @param listener 变更事件监听器，不允许为空
        */
        void unsubscribe(URL url, NotifyListener listener);
        /***
            查询注册列表，与订阅的推模式相对应，这里为拉模式，只返回一次结果。
            * @param url 查询条件，不允许为空，
            如： consumer://10.20.153.10/com.alibaba.foo.BarService?version=1.0.0&application=kylin
            * @return 已注册信息列表，可能为空，
        */
        List<URL> lookup(URL url);
    订阅方法需要一个监听器参数： NotifyListener.java：
        /*** 当收到服务变更通知时触发。
            * @param urls 已注册信息列表，总不为空
        */
        void notify(List<URL> urls);
    register方法就是将url写入注册中心，subscribe 则将监 听器注册到 url 上，当服务 url 有变化时，则触发notify方法。
    当某个服务发生变动，notify触发回来的urls信息也同样包含这些信息 当然，Dubbo此处定义的Registry服务，是个扩展点，
    有很多实现，可心通过SPI 机制适配实现类。 扩展点接口：org.apache.dubbo.registry.RegistryFactory，也就是
    RegistryFactory这个注册工厂接口是一个SPI的扩展接口，可以根据不同url实现不同的注册中心。
    RegistryDirectory这个是消费端的一个监听实现，它实现了NotifyListener接口的notify方法：
        Dubbo的监听实现逻辑类RegistryDirectory，它的包装了Dubbo的发布订阅逻辑并且其本身也是看监听器。
        监听触发逻辑 在 notify 方法中，主要职责便是监听到的 url 信息转化为 invoker 实体，提供给 Dubbo 使用。
        为了性能，在RegistryDirectory中，可以看到有很多的缓存容器，urlInvokerMap/methodInvokerMap/cachedInvokerUrls等用来
        缓存服务的信息。也就是说，notify 的作用是更改这些缓存信息，而 Dubbo 在 rpc 过程中，则是直接使用缓存中的信息。
        这里的监听器是监听，当增加服务端的时候，注册中心将会把全量的url推送给这个监听器。



















