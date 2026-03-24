1.raft 算法原理
    https://blog.csdn.net/wolf_love666/article/details/93014047
2.redis的哨兵模式
3.redis中的集群模式
4.Redis事务
    https://www.cnblogs.com/wxl123/p/11110802.html
    (1)指令
        开启事务 multi
        提交事务 exec
        取消当前队列的所有操作 discard
        事务例子：
            set age 18
            multi
            set age 19
            exec
        这里如果在事务执行的过程中，没有执行exec提交事务，其它线程get age到的值仍然是18，因为redis的事务是使用一个队列
        一次行将命令执行的。
        如果在事务执行的过程中出现异常，则中断事务，但是已经执行的命令不会进行回滚。
    (2)事务的原理
        https://baijiahao.baidu.com/s?id=1613631210471699441&wfr=spider&for=pc
    (3)watch乐观锁,WATCH 命令为事务提供一个check-and-set (CAS) 行为。
        https://www.cnblogs.com/dantes91/p/4901598.html
        例子如下
        t1线程：
            set age 19  (原值)
            watch age   (对age值进行监控)
            multi
            set age 100  (当t1执行到这里的时候，下面的t2线程执行了一条set命令)
            exec (这里则返回(nil)表示失败)
            get age (这里则会返回t2线程set进去的值16)
        t2线程
            set age 16
        get age (这里的值为16，返回的是watch age命令后set age 16的这个值)
        总结：
            watch命令是用来对multi事务中的key进行监听用的，如果其它线程一旦将这个监听的key进行了改变，则当exec提交
            事务的时候就会失败，不会执行事务中对这个监听key的set操作。
            WATCH 可以用来监听事务中的队列中的命令，在EXEC之前，一旦发现有一个命令被修改了的 , 那么整个事务就会终止，
            EXEC返回一个 Null ，提示用户事务失败了
4.哨兵+cluster+9种数据结构及用法+底层的存储结构，面试背下来；
5.ziplist数据结构
6.redis使用scan代替keys
7.redis做异步消息队列
    lpop左边出，rpush右边进，blpop左边阻塞等待，sub和pub发布订阅模式；









