package com.hmdp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmdp.entity.FileHash;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文件判重 Mapper 接口
 * </p>
 *
 * @author hx
 * @since 2023-04-22
 */
@Mapper
public interface FileHashMapper extends BaseMapper<FileHash> {

}
