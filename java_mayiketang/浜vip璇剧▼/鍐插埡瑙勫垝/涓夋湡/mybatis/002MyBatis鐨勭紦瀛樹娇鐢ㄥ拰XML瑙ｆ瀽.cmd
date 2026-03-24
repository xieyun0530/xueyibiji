1.MyBatis分为一级缓存和二级缓存
    （1）一级缓存：默认开启的且缓存范围是sqlSession范围
        http的每次请求都会从新open一个sqlSession对象，所以在分布式环境中一级缓存是不会代理线程安全问题；
    （2）二级缓存：默认关闭，缓存范围是sqlSessionFactory范围
2.使用vfs进行文件扫描，扫描的方式是把package定义的名下的所有.class文件逐个文件读取的方式加载到内存中，这里与spring的文件扫描
    不一样的地方，因为spring扫描是使用递归的方式将.class文件全部扫描到内存中。
3.别名扫描注册，这里将vfs扫描的文件中递归创建别名映射Map缓存；这里typeAliases的参数是什么意思？