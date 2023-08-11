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
 * @author makejava
 * @since 2021-11-18 16:46:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseInfo extends Model {

    private static final long serialVersionUID = -38207001866720024L;

    @ApiModelProperty(value = "【主键标识】")
    private Long id;

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程内容】")
    private String courseContent;

    @ApiModelProperty(value = "【课程积分】")
    private Integer coursePoints;

    @ApiModelProperty(value = "【课程总时长】")
    private Long courseDuration;

    @ApiModelProperty(value = "【课程价格 单位分】")
    private Long coursePrice;

    @ApiModelProperty(value = "【课时管理】")
    private Integer courseManage;

    @ApiModelProperty(value = "【内容模式 1-章节模式 2-节模式 3-无章节模式】")
    private Integer courseMode;

    @ApiModelProperty(value = "【精品课程 1-精品课程 0-普通课程(默认)】")
    private Integer gooded;

    @ApiModelProperty(value = "【最大报名人数 0-无限制】")
    private Integer signUpLimit;

    @ApiModelProperty(value = "【直播开始时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "【直播结束时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "【倍速播放 0禁止 1允许】")
    private Integer playbackSpeed;

    @ApiModelProperty(value = "【拖动进度 0禁止 1允许】")
    private Integer playbackDrag;

    @ApiModelProperty(value = "【可见用户状态 1 所有人可见 2 部分可见】")
    private Integer userVisibleStatus;

    @ApiModelProperty(value = "【评论状态 0关闭评论 1开启评论 】")
    private Integer discussStatus;

    @ApiModelProperty(value = "【虚拟报名人数状态 0-关闭 1-开启】")
    private Integer virtualStatus;

    @ApiModelProperty(value = "【虚拟报名人数】")
    private Integer virtualSignUpNum;

    @ApiModelProperty(value = "【报名验证类型 1-无需验证 2-密码验证】")
    private Integer signUpAuthType;

    @ApiModelProperty(value = "【报名验证值】")
    private String signUpAuthValue;

    @ApiModelProperty(value = "【创建人ID】")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "【创建时间】")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "【最后修改人ID】")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "【最后修改时间】")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
