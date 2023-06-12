package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IBlogService extends IService<Blog> {

    Blog getBlogDetail(Long id);

    /**
     * 点赞笔记
     * @param id 笔记id
     * @return
     */
    Result like(Long id);

    List<UserDTO> likeList(Long id);

    void saveBlog(Blog blog);

    Result queryBolgOfFollow(Long max, int offset);
}
