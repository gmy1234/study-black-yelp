package com.hmdp.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.constants.RedisConstants;
import com.hmdp.entity.ShopType;
import com.hmdp.exception.BusinessException;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<ShopType> queryTypeList() {
        Set<String> shopType = stringRedisTemplate.opsForZSet().rangeByScore(RedisConstants.SHOP_TYPE, 0, 100);
        if (CollUtil.isNotEmpty(shopType)) {
            return shopType.stream()
                    .map(v -> JSONUtil.toBean(v, ShopType.class))
                    .collect(Collectors.toList());
        }
        // redis 不存在
        List<ShopType> shopTypeList = this.lambdaQuery()
                .orderByAsc(ShopType::getSort)
                .list();
        if (CollUtil.isEmpty(shopTypeList)){
            throw new BusinessException("没有商品分类");
        }

        Set<ZSetOperations.TypedTuple<String>> shopSetCache = shopTypeList.stream()
                .map(v -> {
                    String s = JSONUtil.toJsonStr(v);
                    ZSetOperations.TypedTuple<String> objectTypedTuple =new  DefaultTypedTuple<>(s, v.getSort().doubleValue());
                    return objectTypedTuple;
                })
                .collect(Collectors.toSet());

        stringRedisTemplate.opsForZSet().add(RedisConstants.SHOP_TYPE, shopSetCache);
        return null;
    }
}
