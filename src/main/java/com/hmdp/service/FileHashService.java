package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.entity.FileHash;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件判重 服务类
 * </p>
 *
 * @author gmy
 * @since 2023-04-22
 */
public interface FileHashService extends IService<FileHash> {

    void upload(MultipartFile image);

}
