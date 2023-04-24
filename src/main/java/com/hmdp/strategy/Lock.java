package com.hmdp.strategy;

/**
 * @author gmydl
 * @title: Lock
 * @projectName yelp
 * @description: TODO
 * @date 2023/4/24 11:28
 */

public interface Lock {


    /**
     * 尝试获取锁
     * @param timeoutSec 锁的超时时间
     * @return
     */
    boolean tryLock(Long timeoutSec);


    /**
     * 解锁
     */
    void unLock();

}
