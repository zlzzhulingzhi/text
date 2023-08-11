package cn.qbs.wa.train.screen.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-10-09 17:04:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScreenAttendClass extends Model {

    private static final long serialVersionUID = -14274021010744167L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "班级名称")
    private String clazzName;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "教室")
    private String classroom;

    @ApiModelProperty(value = "学员人数")
    private Integer studentNum;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
