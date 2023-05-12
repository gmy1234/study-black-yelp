package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.hmdp.constants.RedisConstants;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Follow;
import com.hmdp.entity.User;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Override
    public void followUser(Long followUserId, Boolean isFollow) {
        Long userId = UserHolder.getUser().getId();
        if (isFollow) {
            // 取关
            boolean remove = this.removeById(lambdaQuery().eq(Follow::getFollowUserId, followUserId)
                    .eq(Follow::getUserId, userId).one().getId());
            if (remove) {
                stringRedisTemplate.opsForSet().remove(RedisConstants.FOLLOW + userId, followUserId.toString());

            }
        }else {
            // 关注
            boolean save = this.save(Follow.builder()
                    .userId(userId)
                    .followUserId(followUserId)
                    .build());
            if (save){
                stringRedisTemplate.opsForSet().add(RedisConstants.FOLLOW + userId, followUserId.toString());
            }
        }
    }

    @Override
    public Boolean isFollow(Long followUserId) {
        Long userId = UserHolder.getUser().getId();
        Integer count = lambdaQuery().eq(Follow::getFollowUserId, followUserId)
                .eq(Follow::getUserId, userId).count();
        return count > 0;

    }

    @Override
    public Result followCommon(Long id) {
        Long userId = UserHolder.getUser().getId();
        // 交集
        Set<String> intersect = stringRedisTemplate.opsForSet()
                .intersect(RedisConstants.FOLLOW + userId, RedisConstants.FOLLOW + id);
        if (CollectionUtil.isEmpty(intersect)){
            return Result.ok();
        }
        List<Long> ids = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
        List<UserDTO> users = userService.listByIds(ids)
                .stream()
                .map(item -> BeanUtil.toBean(item, UserDTO.class))
                .collect(Collectors.toList());

        return Result.ok(users);
    }
}
