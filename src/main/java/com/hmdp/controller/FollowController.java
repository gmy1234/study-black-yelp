package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.service.IFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  关注
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/follow")
@Api("用户之间关注模块")
public class FollowController {

    @Resource
    IFollowService followService;


    /**
     * 关注用户
     */
    @PostMapping("/{id}/{isFollow}")
    public Result followUser(@PathVariable("id") Long followUserId,
                             @PathVariable("isFollow") Boolean isFollow) {

         followService.followUser(followUserId, isFollow);
        return Result.ok();
    }


    @PostMapping("/or/not/{id}")
    @ApiOperation("是否关注")
    public Result isFollow(@PathVariable("id") Long followUserId) {
        return Result.ok(followService.isFollow(followUserId));
    }

    @PostMapping("/common/{id}")
    @ApiOperation("共同关注")
    public Result followCommon(@PathVariable("id") Long id) {
        return Result.ok(followService.followCommon(id));
    }
}
