package com.hmdp.service.impl;

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
        String shopJson = stringRedisTemplate.opsForValue().get(RedisConstants.CACHE_SHOP_KEY + id);
        if (StrUtil.isNotBlank(shopJson)){
            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
            return Result.ok(shop);
        }
        // 命中的是否为""
        if ("".equals(shopJson)) {
            return Result.fail(StatusEnum.NOT_SHOP.getDesc());
        }

        // 不存在，查数据库，并写入空值
        stringRedisTemplate.opsForValue()
                .set(RedisConstants.CACHE_SHOP_KEY + id, "", CACHE_NULL_TTL, TimeUnit.MINUTES);

        Shop shopDB = this.getById(id);
        if (Objects.isNull(shopDB)){
            return Result.fail(StatusEnum.NOT_SHOP.getDesc());
         }

        //存在
        stringRedisTemplate.opsForValue()
                .set(RedisConstants.CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(shopDB), 30, TimeUnit.MINUTES);
        return Result.ok(shopDB);
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
