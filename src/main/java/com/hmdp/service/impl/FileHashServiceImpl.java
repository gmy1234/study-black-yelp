package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hmdp.entity.FileHash;
import com.hmdp.mapper.FileHashMapper;
import com.hmdp.service.FileHashService;
import com.hmdp.utils.ToolUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * <p>
 * 文件判重 服务实现类
 * </p>
 *
 * @author hx
 * @since 2023-04-22
 */
@Service
public class FileHashServiceImpl extends ServiceImpl<FileHashMapper, FileHash> implements FileHashService {

    @Resource
    FileHashService fileHashService;

    @Override
    public void upload(MultipartFile image) {
        try {
            InputStream inputStream = image.getInputStream();
            String hash = ToolUtils.getHash(inputStream);
            FileHash fileHash = fileHashService.getById(hash);
            if (Objects.nonNull(fileHash)) {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
