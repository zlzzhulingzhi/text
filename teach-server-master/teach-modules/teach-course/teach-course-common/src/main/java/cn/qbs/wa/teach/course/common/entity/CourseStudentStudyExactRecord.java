package cn.qbs.wa.teach.course.common.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @author zcm
 * @since 2022-06-23 17:00:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseStudentStudyExactRecord extends Model {

    private static final long serialVersionUID = 663171055545496226L;


    /**
     * 【主键标识】
     */
    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    /**
     * 【组织机构ID】
     */
    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    /**
     * 【课程章节ID】
     */
    @ApiModelProperty(value = "【课程章节ID】")
    private Long chapterId;

    /**
     * 【课程ID】
     */
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    /**
     * 【讲次ID】
     */
    @ApiModelProperty(value = "【讲次ID】")
    private Long lessonId;

    /**
     * 【关联内容ID】
     */
    @ApiModelProperty(value = "【关联内容ID】")
    private Long componentId;

    /**
     * 【学员ID】
     */
    @ApiModelProperty(value = "【学员ID】")
    private Long userId;

    /**
     * 【播放设备】
     */
    @ApiModelProperty(value = "【播放设备】")
    private String playbackDevice;

    /**
     * 【播放位置】
     */
    @ApiModelProperty(value = "【播放位置】")
    private Integer playbackPosition;

    /**
     * 【播放进度】
     */
    @ApiModelProperty(value = "【播放进度】")
    private Integer playbackProgress;

    /**
     * 【学习时长】
     */
    @ApiModelProperty(value = "【学习时长】")
    private Integer learningDuration;

    /**
     * 【创建人ID】
     */
    @ApiModelProperty(value = "【创建人ID】")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 【创建时间】
     */
    @ApiModelProperty(value = "【创建时间】")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 【最后修改人ID】
     */
    @ApiModelProperty(value = "【最后修改人ID】")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    /**
     * 【最后修改时间】
     */
    @ApiModelProperty(value = "【最后修改时间】")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
