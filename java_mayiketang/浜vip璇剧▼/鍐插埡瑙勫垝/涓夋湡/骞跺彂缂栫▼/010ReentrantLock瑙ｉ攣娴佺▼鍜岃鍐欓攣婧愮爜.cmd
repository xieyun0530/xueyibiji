1.为什么读写锁不支持锁升级？
    因为会出现死锁，情况如下：
    reentrantReadWriteLock.rLock();
        to...n行业务代码
        reentrantReadWriteLock.wLock();
        to...n行代码
        reentrantReadWriteLock.wLock();
    reentrantReadWriteLock.rLock();
    这里如果只有t1一个线程进行加锁是没有问题的，但是如果当t1拿到读锁在执行读锁下面的业务代码的时候，t2来了t2获取
    到读锁，这个时候t2比t1先执行完读锁下面的业务代码，去获取写锁，这个时候就会获取不到因为读写互斥，当t1执行完
    业务代码后去获取写锁，这个时候也获取不到，因为t2获取了读锁，导致读写互斥，就会出现t1等待t2释放读锁，t2则又
    等待t1释放读锁的情况，因此导产生了一个死锁。
2.为什么读写锁支持锁降级？
    这里如果弄明白了读写锁为什么不支持升级，相信支持锁降级的说法也就很容易了，看下面一段代码；
    reentrantReadWriteLock.WLock();
        to...n行业务代码
        reentrantReadWriteLock.rLock();
        to...n行代码
        reentrantReadWriteLock.rLock();
    reentrantReadWriteLock.WLock();
    这里如果t1进来获取到写锁，同样t1在执行写锁下面的业务代码的时候，t2来了但是这里t2是获取不到写锁的，因为写写互斥，
    所以说读写锁支持锁的降级。
3.读读并发
    当唤醒一个正在等待队列中的读锁的时候，如果后面节点的线程也是读锁，则会将它也唤醒，但是如果后面节点的线程是写锁则
    不会唤醒，总结成一句话就是唤醒读锁的时候会唤醒多个连续的读锁，直到遇到一个写锁止。
4.为什么在park线程唤醒后，会执行Thread.interrupted()；
     private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
     }
     看上面的代码，这里是将中断标志位设置为true；
     这里分为两种情况，一种是lock加锁的情况：
     if (shouldParkAfterFailedAcquire(p, node) &&
                         parkAndCheckInterrupt())
                         interrupted = true;
     这里parkAndCheckInterrupt返回true就会导致acquireQueued返回true，然后在acquire()方法中会执行selfInterrupt()
     方法，这里的方法是将中断标志位设置为true；这里的设计也就是没有改变用户设置的中断标志，且造成一种没有响应中断
     的一种假象，其实内部是响应了中断的。
     另一种是lockInterruptibly加锁的情况：
     if (shouldParkAfterFailedAcquire(p, node) &&
                         parkAndCheckInterrupt())
                         throw new InterruptedException();
     这里就比较简单了，直接是抛出了异常，让用户进行捕捉这个异常，用户可以在这个异常中做任何事情，但是这里没有清除
     中断标志，需要用户进行手动清除，方便用户做一些操作后再进行清除。