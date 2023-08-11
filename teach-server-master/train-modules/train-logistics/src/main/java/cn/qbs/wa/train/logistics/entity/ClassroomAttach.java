package cn.qbs.wa.train.logistics.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author makejava
 * @since 2022-10-12 15:36:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClassroomAttach extends Model {

    private static final long serialVersionUID = -32446751622467628L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "教室ID")
    private Long classroomId;

    @ApiModelProperty(value = "文件类型(pic-图片 doc-文件)")
    private String fileType;

    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
