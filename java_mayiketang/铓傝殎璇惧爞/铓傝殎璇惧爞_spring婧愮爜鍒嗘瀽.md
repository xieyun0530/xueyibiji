1.Spring源码分析
   (1)Spring源码思路
      SpringIOC-----控制反转 依赖注入 反射机制+dom4j
      SpringAOp-----面向切面编程 动态代理(cglib asm字节码)
      代理设计模式 静态代理 动态代理 jdk动态代理 cglib动态代理 字节码asm
      SpringBean的生命周期
      SpringMVC执行流程
      Mybatis会话工厂源码分析
      分析源码之前，整理api用法
   (2)SpringIOC源码分析
      反射机制+dom4j
      1.dom4j解析xml文件，使用beanId查找bean是否存在
      2.存在的话，获取该节点的class地址
      3.使用反射机制初始化
      BeanFactory-------定义SpringIOC的基本形式
      Spring的核心jar:
        SpringCore--------Spring的核心jar
        SpringContext-----上下文IOC具体实现
        Spring-jdbc-------Springjdbc
        SpringAop---------面向切面编程
        SpringBeans-------Spring实例
   
        
      