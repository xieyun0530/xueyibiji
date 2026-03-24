1.sql优化
select a.domain_id as domainId,b.data_model_id as dataModelId,c.`name` as serviceName, d.`name` as bucketName,b.`name` as strategyName,b.status,b.release_version as version
from domain_data_model a
inner join ds_strategy b on a.id = b.data_model_id and b.`status` != 9
inner join ds_service c on b.decision_id = c.id  and c.delete_flag = 1
inner join domain_bucket d on b.bucket_id = d.id and d.delete_flag = 1
where a.delete_flag = 1 and a.domain_id = 10153000

select a.domain_id as domainId,b.data_model_id as dataModelId,c.`name` as serviceName, d.`name` as bucketName,b.`name` as strategyName,b.status,b.release_version as version
from domain_data_model a
inner join ds_strategy b on a.id = b.data_model_id and b.`status` != 9
inner join ds_service c on b.decision_id = c.id  and c.delete_flag = 1
inner join domain_bucket d on b.bucket_id = d.id and d.delete_flag = 1
where a.delete_flag = 1 and d.domain_id = 10153000

为什么where后的条件使用a.domain_id = 10153000 b表能走索引，而使用d.domain_id = 10153000 b表却走不了索引？
分析：
a、b两个表inner join 的on条件下放到where，先执行a表where a.domain_id=''，这个查询必然是走索引的（细看了下，domain_id建了索引），拿到的结果集在索引上是有序的，
然后再执行b表的查询where b.data_model_id=a.id 必然也是索引上有序的，所以用a.domain_id做查询条件是走索引的；
把多个inner join 拆分单个执行，每次执行完拿生成的临时表去跟下一个inner join做关联，单个inner join 的执行等于where，on后面的条件等同于where；
你b走索引的时候，是先执行a表的，a作为驱动表使用nlp吧，通过id去查询b的data_model_id，没走索引是先查询的d表，之后根据bucket_id去查询的

为什么他执行两次sql，explain中table的顺序会变？
分析：
顺序我是不知道，只能认为where后面的条件改变了驱动表吧，因为你有d的筛选条件，所以认为d是小的驱动表，所以先搞他


mysql的表关联常见有两种算法
Nested-Loop Join 算法
Block Nested-Loop Join 算法
1、 嵌套循环连接 Nested-Loop Join(NLJ) 算法
一次一行循环地从第一张表（称为驱动表）中读取行，在这行数据中取到关联字段，根据关联字段在另一张表（被驱动表）里取出满足条件的行，然后取出两张表的结果合集。
mysql> EXPLAIN select * from t1 inner join t2 on t1.a= t2.a;

从执行计划中可以看到这些信息：
驱动表是 t2，被驱动表是 t1。先执行的就是驱动表(执行计划结果的id如果一样则按从上到下顺序执行sql)；优化器一般会优先选择小表做驱动表，用where条件过滤完驱动表，然后再跟被驱动表做关联查询。所以使用 inner join 时，排在前面的表并不一定就是驱动表。
当使用left join时，左表是驱动表，右表是被驱动表，当使用right join时，右表时驱动表，左表是被驱动表，当使用join时，mysql会选择数据量比较小的表作为驱动表，大表作为被驱动表。
使用了 NLJ算法。一般 join 语句中，如果执行计划 Extra 中未出现 Using join buffer 则表示使用的 join 算法是 NLJ。
上面sql的大致流程如下：
从表 t2 中读取一行数据（如果t2表有查询过滤条件的，用先用条件过滤完，再从过滤结果里取出一行数据）；
从第 1 步的数据中，取出关联字段 a，到表 t1 中查找；
取出表 t1 中满足条件的行，跟 t2 中获取到的结果合并，作为结果返回给客户端；
重复上面 3 步。
整个过程会读取 t2 表的所有数据(扫描100行)，然后遍历这每行数据中字段 a 的值，根据 t2 表中 a 的值索引扫描 t1 表中的对应行(扫描100次 t1 表的索引，1次扫描可以认为最终只扫描 t1 表一行完整数据，也就是总共 t1 表也扫描了100行)。因此整个过程扫描了 200 行。
如果被驱动表的关联字段没索引，使用NLJ算法性能会比较低(下面有详细解释)，mysql会选择Block Nested-Loop Join算法。

2、 基于块的嵌套循环连接 Block Nested-Loop Join(BNL)算法
把驱动表的数据读入到 join_buffer 中，然后扫描被驱动表，把被驱动表每一行取出来跟 join_buffer 中的数据做对比。
mysql>EXPLAIN select * from t1 inner join t2 on t1.b= t2.b;

Extra 中 的Using join buffer (Block Nested Loop)说明该关联查询使用的是 BNL 算法。
上面sql的大致流程如下：
把 t2 的所有数据放入到 join_buffer 中
把表 t1 中每一行取出来，跟 join_buffer 中的数据做对比
返回满足 join 条件的数据
整个过程对表 t1 和 t2 都做了一次全表扫描，因此扫描的总行数为10000(表 t1 的数据总量) + 100(表 t2 的数据总量) = 10100。并且 join_buffer 里的数据是无序的，因此对表 t1 中的每一行，都要做 100 次判断，所以内存中的判断次数是 100 * 10000= 100 万次。
这个例子里表 t2 才 100 行，要是表 t2 是一个大表，join_buffer 放不下怎么办呢？·
join_buffer 的大小是由参数 join_buffer_size 设定的，默认值是 256k。如果放不下表 t2 的所有数据话，策略很简单，就是分段放。
比如 t2 表有1000行记录， join_buffer 一次只能放800行数据，那么执行过程就是先往 join_buffer 里放800行记录，然后从 t1 表里取数据跟 join_buffer 中数据对比得到部分结果，然后清空 join_buffer ，再放入 t2 表剩余200行记录，再次从 t1 表里取数据跟 join_buffer 中数据对比。所以就多扫了一次 t1 表。

被驱动表的关联字段没索引为什么要选择使用 BNL 算法而不使用 Nested-Loop Join 呢？
如果上面第二条sql使用 Nested-Loop Join，那么扫描行数为 100 * 10000 = 100万次，这个是磁盘扫描。
很显然，用BNL磁盘扫描次数少很多，相比于磁盘扫描，BNL的内存计算会快得多。
因此MySQL对于被驱动表的关联字段没索引的关联查询，一般都会使用 BNL 算法。如果有索引一般选择 NLJ 算法，有索引的情况下 NLJ 算法比 BNL算法性能更高
对于关联sql的优化
关联字段加索引，让mysql做join操作时尽量选择NLJ算法
2.接口优化
	jsonSchema数据递归采用尾递归，从8秒优化到1秒以内；
3.根据主键查询数据模型查询慢