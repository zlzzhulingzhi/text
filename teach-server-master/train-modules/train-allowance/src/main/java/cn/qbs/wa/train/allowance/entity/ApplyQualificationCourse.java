package cn.qbs.wa.train.allowance.entity;

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
 * @since 2022-11-02 11:20:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyQualificationCourse extends Model {

    private static final long serialVersionUID = -42243773800915388L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "资质申请项目表ID")
    private Long projectId;

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "课程名称(快照)")
    private String courseName;

    @ApiModelProperty(value = "培训对象")
    private String trainObject;

    @ApiModelProperty(value = "培训内容及目标")
    private String trainContent;

    @ApiModelProperty(value = "讲师ID")
    private Long lecturerId;

    @ApiModelProperty(value = "主讲老师")
    private String lecturerName;

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
