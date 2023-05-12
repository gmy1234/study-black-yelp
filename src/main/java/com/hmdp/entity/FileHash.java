package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 文件判重
 * </p>
 *
 * @author gmy
 * @since 2023-04-22
 */
@Data
@TableName("file_hash")
@ApiModel(value = "FileHash对象", description = "文件判重")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class FileHash implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("哈希值")
	@TableId("id")
    private String id;

    @ApiModelProperty("文件原始名称")
    @TableField("original_file_name")
    private String originalFileName;

    @ApiModelProperty("minio存储名称")
    @TableField("minio_file_name")
    private String minioFileName;

	@ApiModelProperty("上传人")
	@TableField("uploader")
	private String uploader;

    @ApiModelProperty("是否删除，0-否，1-是")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

}
