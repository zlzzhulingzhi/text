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
 * @since 2022-10-09 17:04:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScreenStatDataDynamic extends Model {

    private static final long serialVersionUID = 469112164096428388L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "模块(classroom-教室 dormitory-宿舍)")
    private String module;

    @ApiModelProperty(value = "数据名称")
    private String dataName;

    @ApiModelProperty(value = "已使用数量")
    private Integer usingNum;

    @ApiModelProperty(value = "空闲数量")
    private Integer freeNum;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "展示状态(0-不展示 1-展示)")
    private Integer display;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
