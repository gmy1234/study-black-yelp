package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.constants.RedisConstants;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.enums.StatusEnum;
import com.hmdp.exception.BusinessException;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.hmdp.constants.RedisConstants.CACHE_NULL_TTL;
import static com.hmdp.constants.RedisConstants.LOCK_SHOP_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Result queryShopById(Long id) {
        // 缓存穿透
        Shop shop = this.queryWithPassThrough(id);
        return Result.ok(shop);
    }


    /**
     * 解决缓存穿透问题，查询逻辑
     * @param id
     * @return
     */
    public Shop queryWithPassThrough(Long id){
        String shopJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
        if (StrUtil.isNotBlank(shopJson)){
            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
            return shop;
        }
        // 命中的是否为""
        if ("".equals(shopJson)) {
            return null;
        }
        // 不存在，查数据库，并写入空值
        Shop shopDB = this.getById(id);
        if (Objects.isNull(shopDB)){
            stringRedisTemplate.opsForValue()
                    .set(RedisConstants.CACHE_SHOP_KEY + id, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        //存在
        stringRedisTemplate.opsForValue()
                .set(RedisConstants.CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(shopDB), 30, TimeUnit.MINUTES);
        return shopDB;
    }

    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "1", 10, TimeUnit.MICROSECONDS);

        return BooleanUtil.isTrue(flag);
    }

    private void unLock(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * 解决缓存击穿
     * @param id 商品ID
     * @return 商品
     */
    public Shop queryWithMutex(Long id){
        String shopJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
        if (StrUtil.isNotBlank(shopJson)){
            return JSONUtil.toBean(shopJson, Shop.class);
        }
        // 命中的是否为""
        if ("".equals(shopJson)) {
            return null;
        }

        // 实现
        // 获取互斥锁,
        Shop shop = null;
        //失败休眠
        try {
            boolean isLock = tryLock(LOCK_SHOP_KEY + id);
            if (!isLock){
                Thread.sleep(50);
                return this.queryWithMutex(id);
            }
            // 获取锁后第二次检查是否命中
            String shopCache = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
            if (StrUtil.isNotBlank(shopCache)){
                return JSONUtil.toBean(shopJson, Shop.class);
            }
            // 成功继续
            shop = this.getById(id);

            if (Objects.isNull(shop)){
                stringRedisTemplate.opsForValue()
                        .set(RedisConstants.CACHE_SHOP_KEY + id, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            //存在, 写redis
            stringRedisTemplate.opsForValue()
                    .set(RedisConstants.CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(shop), 30, TimeUnit.MINUTES);

        }catch (RuntimeException | InterruptedException e){
            e.printStackTrace();
        }finally {
            unLock(LOCK_SHOP_KEY + id);
        }

        return shop;
    }


    @Override
    @Transactional
    public void updateShop(Shop shop) {
        Long id = shop.getId();
        if (Objects.isNull(id)){
            throw new BusinessException(StatusEnum.NOT_SHOP_ID);
        }
        // 更新数据库
        this.updateById(shop);

        // 删缓存
        stringRedisTemplate.delete(RedisConstants.CACHE_SHOP_KEY + id);
    }
}
