package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.constants.RedisConstants;
import com.hmdp.constants.SystemConstants;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserService userService;

    @Override
    public Blog getBlogDetail(Long id) {
        Blog blog = this.getById(id);
        if (Objects.isNull(blog)){
            return null;
        }
        return blog;
    }

    private Blog isBlogLike(Blog blog){
        UserDTO user = UserHolder.getUser();
        if (Objects.isNull(user)){
            return blog;
        }
        Long userId = user.getId();
        Double isLike = stringRedisTemplate.opsForZSet().score(RedisConstants.BLOG_LIKED_KEY + blog.getId(), userId.toString());
        blog.setIsLike(Objects.nonNull(isLike));
        return blog;
    }

    @Override
    public Result like(Long id) {
        // 获取用户信息
        Long userId = UserHolder.getUser().getId();
        // 判断是否点过赞
        Double score = stringRedisTemplate.opsForZSet().score(RedisConstants.BLOG_LIKED_KEY + id, userId.toString());
        if (Objects.nonNull(score)) {
            // 点过赞 ，取消点赞
            boolean success = this.lambdaUpdate().setSql("liked = liked - 1").eq(Blog::getId, id).update();
            if (success) {
                stringRedisTemplate.opsForZSet().remove(RedisConstants.BLOG_LIKED_KEY + id, userId.toString());
            }
        }else {
            // 没点过 + 1
            boolean success = this.lambdaUpdate().setSql("liked = liked + 1").eq(Blog::getId, id).update();
            if (success) {
                stringRedisTemplate.opsForZSet().add(RedisConstants.BLOG_LIKED_KEY + id, userId.toString(), System.currentTimeMillis());
            }
        }

        return Result.ok();
    }

    @Override
    public List<UserDTO> likeList(Long id) {
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(RedisConstants.BLOG_LIKED_KEY + id, 0, 4);
        if (CollectionUtil.isEmpty(top5)){
            return new ArrayList<>();
        }
        List<Long> userIdFive = top5.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
        String ids = StrUtil.join(",", userIdFive);
        userService.lambdaQuery().in(User::getId).last("order by field(id," + ids + ")");
        return userService.listByIds(userIdFive).stream()
                .map(item -> BeanUtil.copyProperties(item, UserDTO.class))
                .collect(Collectors.toList());

    }


}
