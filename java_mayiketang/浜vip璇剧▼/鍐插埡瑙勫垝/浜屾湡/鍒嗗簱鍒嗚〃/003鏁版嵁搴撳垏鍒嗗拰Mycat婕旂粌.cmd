1.数据库切分规则
    (1)垂直切分
        优点：
            数据库的拆分简单明了，拆分规则明确；
            应用程序模块清晰明确，整合容易；
            数据维护方便易行，容易定位；
        缺点：
            跨库join问题；
            对于数据量大的表不一定能满足要求；
            事务处理非常复杂；
            切分达到一定程度之后，扩展性会遇到限制；
            过多切分可能会带来系统过渡复杂而难以维护；
    (2)水平拆分
        优点：
            表关联基本能够在数据库端全部完成；
            不会出现某些超大表的瓶颈；
            应用端整体架构改动少；
            事务处理简单；
            只要切分规则能够定义好，基本上较难遇到扩展性限制；
        缺点：
            切分规则复杂，很难抽象出一个能够满足整个数据库切分的规则；
            后期数据的维护难度有所增加，人为手工定位数据更困难；
            应用系统各模块耦合度较高，可能会对后面数据的迁移拆分造成一定的困难；
            跨节点合并排序分页问题；
            多数据源管理问题；
    (2)数据库分片规则
        1.按照id取模的方式；
        2.按日期，将不同月份的数据放到不同的数据库中；
    (3)切分带来的问题
        1.引入分布式事务的问题；
        2.跨节点join的问题；
        3.跨节点合并排序分页问题；
2.MyCat
    (1)MyCat中的概念
        逻辑库(Schema);存MyCat中的虚拟库；
        逻辑表(table)：存在MyCat中的虚拟表
        分片表：分片表，是指那些原有的很大数据的表，需要切分到多个数据库的表，这样，每个分片都有一部分 数据，所有分片构成了完整的数据；
        非分片表：不需要进行数据切分的表；
        ER表：子表的记录与所关联的父表记录存放在同一个数据分片上，即子表依赖于父表，通过表分组（Table Group）保证数据Join不会跨库操作。
            表分组（Table Group）是解决跨分片数据join的一种很好的思路，也是数据切分规划的重要一条规则
        全局表：例如字典表，每一个数据分片节点上有保存了一份字典表数据 数据冗余是解决跨分片数据join的一种很好的思路，
            也是数据切分规划的另外一条重要规则；
        分片节点(dataNode)：数据切分后，一个大表被分到不同的分片数据库上面，每个表分片所在的数据库就是分片节点；
        节点主机(dataHost)：数据切分后，每个分片节点（dataNode）不一定都会独占一台机器，同一机器上面可以有多个分片数据库，
            这样一个或多个分片节点（dataNode）所在的机器就是节点主机（dataHost）,为了规避单节点主机 并发数限制，尽量将
            读写压力高的分片节点（dataNode）均衡的放在不同的节点主机（dataHost）；
            写入节点(writeHost)，读取节点(readHost)，这里如果配置两个writeHost当其中一台出现故障时可以进行切换；
        分片规则(rule)：前面讲了数据切分，一个大表被分成若干个分片表，就需要一定的规则，这样按照某种业 务规则把数据分到某个
            分片的规则就是分片规则，数据切分选择合适的分片规则非常重要， 将极大的避免后续数据处理的难度。
        全局序列号(sequence)：数据切分后，原有的关系数据库中的主键约束在分布式条件下将无法使用，因此需要引入 外部机制保证数据
            唯一性标识，这种保证全局性的数据唯一标识的机制就是全局序列号（sequence）；
        注意：myCat不支持3表以上的join查询；
3.MyCat的全局序列号
    (1)文件方式获取全局序列号
        文件中配置的是一些id生成规则；参数有最小值、最大值、当前值；对server.xml配置文件中sequnceHandlerType设置为1；
    (2)数据库方式获取全局序列号
        这里有三个参数名称，当前值 ，增长步长，插入针对order表的序列，下面就是去100个序列号：
        INSERT INTO MYCAT_SEQUENCE(name,current_value,increment) VALUES ('ORDER', 100000, 100);
        可理解为mycat在数据库中一次读取多少个sequence. 当这些用完后, 下次再从数据库中读取.
    (3)创建相关function
        1.获取当前sequence的值 (返回当前值,增量)；
        2.设置 sequence 值；
        3.取下一个sequence的值；
        前面3个都是创建函数的sql；还需要对server.xml配置文件中sequnceHandlerType设置为1；
    (4)基于zookeeper生成全局序列
        1.在myid.properties配置文件中配置zk信息；
        2.sequence_distributed_conf.properties配置中文件中，配置成从zk中获取实例id，还可以设置集群id；
        3.server.xml配置文件中sequnceHandlerType设置为3；
    (5)基于时间戳
        server.xml配置文件中sequenceHandlerType设置为1；
4.MyCat分片
    (1)分片枚举
        通过在配置文件中配置可能的枚举id，自己配置分片，适用于特定的业务场景，比如有些业务按省份或者市区保存；
    (2)固定hash分片
        取id的二进制的低10位&1111111111(10个1)，使用一个1024的区间，假设分为三个分区:0-255,256-511,512-1023；
        user_id的低10位二进制&1111111111后根据以上范围落入指定的区域。假如连续插入1-10时会被分配到0-255个分区中，
        也就是同一个机器中，这样的方式减少了插入事务的控制，因为被分配到了同一个数据库中；
    (3)范围约定
        此分片适用于，提前规划好分片字段某个范围属于哪个分片；
    (4)按日期分(天)分片
        参数有一个开始日期、一个结束日期(但是这个参数没有用，因为就算到了结束日期还会再按天数去路由)、天数；
        比如：开始日期2020-01-01，结束日期2021-01-01，天数5天，则1-5号先放入数据库A，6-10号放入数据库B，
        如果只有两个数据库，则11-15会再次放入数据库A，依次循环；
    (5)hash取模：对id进行hash取模的方式
    (6)一致性hash
        1.创建一个环形结构；环形大小为0到2的32次方减1；
        2.将机器放入环形中的某个位置，对数据库的ip进行虚拟映射(比如：数据A[A1，A2,A3])，将虚拟的数据库也放入到
          环形结构中；
        3.将数据放入到环形结构中，以顺时针的方式到这个机器(包括虚拟机器)的数据，这些数据放入到这个机器中；
        总结：这里使用了虚拟映射的方式来解决了，数据分布均匀问题；这些放入到虚拟机器中的数据实际是通过这个虚拟
            映射关系放入到了真实的数据库中；
            这里可以使用这个一致性hash的虚拟映射思想去解决，redis中热点key的问题，例如：rDKey[rDKey1，rDKey2..]
            这里用户进行访问的时候，取缓存的key时使用rDKey，然后使用某种算法去取数组中某个下标的rDKey1，使用
            这个key去访问真实的数据，这里数组中的key对应的value值是相同的；
5.全局表
    1.mycat中把一个表做成一个全局表，需要满足几个特征：
        数据量少；每一个分片都需要用到，往往是系统字典表，比如存一些430--已完成，这样的状态对应表；
    2.为了避免跨库join，所以每个节点都会存储一份；
    3.相应的配置：type="global"：表示开启全局表配置；
        <table name="t_order_type" primaryKey="orderType" dataNode="dn140,dn141" type="global"/>；
    4.当插入数据的时候所有节点中的表都会插入一份数据；
    5.查询的时候只会随机的选择一台节点去查询数据；
6.ER分片表
    1.在mycat ER分片表，需要满足几个特点：
        表和表之间存在主从关系；存在外键关联；
    2.相应的配置：fetchStoreNodeByJdbc：启用ER表使用JDBC方式获取DataNode
      <table name="t_order" dataNode="dn140,dn141"  primaryKey="orderId" rule="jch" fetchStoreNodeByJdbc="true">
      			<childTable name="t_order_detail" primaryKey="orderDetailId" joinKey="orderId" parentKey="orderId"/>
      </table>
    3.插入数据的时候主表和外键表的数据会插入到同一个数据库中；
    4.如果使用非ER表插入后，使用join查询，会存在丢失数据的问题，因为mycat只会按照各自的主机去join查询；
7.Share Join解决跨库join
    利用Share join方式进行跨库JOIN，原理就是解析SQL语句，拆分成单表的SQL语句执行，然后各个节点的数据汇集；
    /*!mycat:catlet=io.mycat.catlets.ShareJoin */select b.* from t_order a join t_order_detail b on a.orderId=b.orderId;
8.Mycat命令行监控；这里命令行监控的端口是9066，客户端的是8066；
9.Mycat-web界面监控工具；
10.Mycat数据扩容后，数据迁移(因为扩容后，需要重新进行数据分配)
    1.新建用于扩容的newSchema.xml和newRule.xml文件，这个文件名称必须是这个两个一样，因为源码中写死了；
    2.修改dataMigrate.sh文件；该文件类型是dos的，需要修改为unix类型；
        查看.sh文件类型：:set ff；修改.sh文件类型：:set ff=unix  回车；
    3.修改dataMigrate.sh中的配置；基本上修改一个即可
        #mysql bin路径
        RUN_CMD="$RUN_CMD -mysqlBin=/usr/bin"
        这个是mysqldump文件的路径，
        找该文件：find / -name mysqldump
    4.修改migrateTables.properties配置文件，指定需要迁移的逻辑库和表；
    5.Mycat扩容注意事项：
        1、jdk不能用openJdk，需要自己安装，配置环境变量
        2、之前的老的schema.xml和rule.xml老配置不能动
        3、新增迁移后的节点新配置newSchema.xml和newRule.xml
        4、mycat bug  migrateTables.properties中逻辑库的名称不能既有大些又有小写，如：enjoyDB，必须全小写或全大写。
        5、迁移类：DataMigrator
11.MyCat弱XA事务
    (1)XA协议原理
        XA协议语法：
        XA start 'any_unique_id';//any_unique_id是一个全局唯一id如：123；
        insert等sql语句xxxxxx;
        XA end 'any_unique_id';
        XA prepare 'any_unique_id';//预提交，这里会有一个返回值，使用这个返回值来做分布式事务协调；
        XA commit 'any_unique_id';
        XA rollback 'any_unique_id';
        XA recover;//查看本机 mysql 目前有哪些 xa 事务处于 prepare 状态；
        总结：
            mysql数据库是支持XA协议的，sqlServer和oracle也支持这个XA协议；
            XA协议特点：同一个会话（同一个用户）跟普通是互斥的；有隔离性；就是具体事务的特性；
    (2)myCat使用XA事务的两段提交
        1.第一阶段，开启XA事务XA start，执行完sql后 XA end，再 XA prepare这里会接受到一个返回值；
        2.等待所有的数据库服务都返回了prepare的返回值，得到所有的返回值；
        3.第二阶段，判断所有的服务的prepare的返回值全部正确后，如果正确才则异步调用XA commit方法，反之XA rollback；
        总结：XA协议的性能不高，因为如果其中一个数据库的prepare执行时间较长，会占用长时间锁住数据；
                如果需要性能高只能保证最终一致性，而XA则是保证的事实一致性，使用MQ可以做到分布式事务的最终一致性；
    (3)为什么说MyCat是弱XA事务；
        因为第二阶段commit的时候，若某个节点除了错，也无法恢复到以后去做recover(健康)操作重新commit，只会回滚；
        但是考虑到当所有的节点执行成功，commit失败的概率很小，因此弱XA事务也已经满足了大部分的需求，性能接近普通事务；
    (4)myCat使用XA协议来完成分布式事务问题；
        myCat使用XA事务的使用和普通事务是一样的，都是加上@Transactional注解，这里会改写doBegin方法，在开启事务的时候
        会对每一台数据库服务发送一个关闭自动事务提交的命令，执行完sql后也进行预提交，预提交后则会进行判断所有的返回值
        然后根据返回值来判断是是否提交还是回滚。
    (5)myCat的高可用
        1.创建两个MyCat服务，使用haproxy做负载，这个haproxy会进行故障切换，也可以配置负载测试，如轮训策略，
            为什么不使用nxgin做负载，因为nxgin只能做http层的负载，而MyCat是tcp层的，因此需要使用haproxy；
        2.使用keepalive的虚拟地址映射来保证haproxy负载的高可用；










































