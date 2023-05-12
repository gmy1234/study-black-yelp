package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IFollowService extends IService<Follow> {

    void followUser(Long followUserId, Boolean isFollow);

    Boolean isFollow(Long followUserId);

    /**
     * 共同关注
     * @param id 目标用户
     * @return
     */
    Result followCommon(Long id);
}
