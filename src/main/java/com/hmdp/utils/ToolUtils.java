package com.hmdp.utils;

import com.hmdp.entity.FileHash;
import com.hmdp.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Objects;

/**
 * @author gmydl
 * @title: ToolUtils
 * @projectName yelp
 * @description: TODO
 * @date 2023/5/12 10:03
 */

@Slf4j
public class ToolUtils {





    public static String getHash(InputStream fis) {
        return fileHash(fis);
    }

    public static String getHash(String path) throws IOException {
        return fileHash(Files.newInputStream(Paths.get(path)));
    }


    /**
     * 将文件流加入缓冲区，计算哈希值，默认md5计算，可改写。文件小于2G 缓冲区效率最高，大于2G可使用NIO
     * @param fis 文件流，文件名不同，文件内容相同，哈希值相同
     * @return 32位哈希值
     */
    private static String fileHash(InputStream fis) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192 * 10];
            int len = 0;
            // 使用md5 计算哈希值
            MessageDigest md = MessageDigest.getInstance("MD5");
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
            return bi.toString(16);
        } catch (Exception e) {
            log.error("file hash error", e);
            throw new BusinessException("file hash error");
        }
    }
}
