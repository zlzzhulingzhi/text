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
 * @since 2022-10-12 19:03:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SceneDevice extends Model {

    private static final long serialVersionUID = 106231965188041499L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "类别(Classroom-教室申请，Dormitory-宿舍申请)")
    private String category;

    @ApiModelProperty(value = "设备名字")
    private String deviceName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序序号")
    private Integer sort;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

}
