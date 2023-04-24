package com.hmdp.strategy;

/**
 * @author gmydl
 * @title: SimpleRedisLock
 * @projectName yelp
 * @description: TODO
 * @date 2023/4/24 11:30
 */
public class SimpleRedisLock implements Lock{
    @Override
    public boolean tryLock(Long timeoutSec) {
        return false;
    }

    @Override
    public void unLock() {

    }
}
