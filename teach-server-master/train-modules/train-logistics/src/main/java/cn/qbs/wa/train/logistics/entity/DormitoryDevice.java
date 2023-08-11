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
 * @since 2022-10-13 09:48:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DormitoryDevice extends Model {

    private static final long serialVersionUID = -90571538629013827L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "宿舍ID")
    private Long dormitoryId;

    @ApiModelProperty(value = "设备ID")
    private Long sceneDeviceId;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
