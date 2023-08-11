package cn.qbs.wa.teach.course.standard.entity;


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
 * @since 2022-12-22 14:01:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TBanner extends Model {

    private static final long serialVersionUID = 582832541511364975L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "数据板块(course-课程 activity-活动)")
    private String section;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "启用状态 0-未启用 1-启用")
    private Integer enabled;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
