package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseInfoDTO extends CourseLiveDTO {

    @ApiModelProperty(value = "【机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【课程封面】")
    private String coverUrl;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "【课程简介】")
    private String introduction;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "【课程分类列表】")
    private List<CourseCategoryDTO> categories;

    @ApiModelProperty(value = "讲师列表")
    private List<CourseLecturerDTO> lecturers;

    @ApiModelProperty(value = "【倍速播放 0禁止 1允许】")
    private Integer playbackSpeed;

    @ApiModelProperty(value = "【拖动进度 0禁止 1允许】")
    private Integer playbackDrag;

    @ApiModelProperty(value = "【内容模式 1-章节模式 2-节模式 3-无章节模式】")
    private Integer courseMode;

    @ApiModelProperty(value = "【课程评分】")
    private BigDecimal score;

    @ApiModelProperty(value = "【报名人数】")
    private Integer signUpNum;

    @ApiModelProperty(value = "【课时数】")
    private Integer lessonNum;

    @ApiModelProperty(value = "【学习总人数】")
    private Integer studyTotalNum;

    @ApiModelProperty(value = "【浏览量】")
    private Integer views;

    @ApiModelProperty(value = "【课程时长】")
    private Long courseDuration;

    @ApiModelProperty(value = "【课程难度】")
    private String difficulty;

    @ApiModelProperty(value = "加入课程标识 true-已加入 false-未加入")
    private Boolean isSignUp;

    @ApiModelProperty(value = "【课时管理】")
    private Integer courseManage;

    @ApiModelProperty(value = "直播ID")
    private Long liveId;

    @ApiModelProperty(value = "【课程积分】")
    private Integer coursePoints;

    @ApiModelProperty(value = "【课程价格 单位分】")
    private Long coursePrice;

    @ApiModelProperty(value = "【最大报名人数 0-无限制】")
    private Integer signUpLimit;

    @ApiModelProperty(value = "最近一次学习录播课程的讲次ID")
    private Long lessonId;

    @ApiModelProperty(value = "课程对应的商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "售罄标识 true-已售罄 false-未售罄")
    private Boolean sellOut = Boolean.FALSE;

    @ApiModelProperty(value = "【可见用户状态 1 所有人可见 2 部分可见】")
    private Integer userVisibleStatus;

    @ApiModelProperty(value = "【可见课程状态 1 用户可见该课程】")
    private Integer courseVisibleStatus;

    @ApiModelProperty(value = "【虚拟报名人数状态 0-关闭 1-开启】")
    private Integer virtualStatus;

    @ApiModelProperty(value = "课程学习总进度")
    private Integer learningRate;

    @ApiModelProperty(value = "【虚拟报名人数】")
    private Integer virtualSignUpNum;

    @ApiModelProperty(value = "【报名验证类型 1-无需验证 2-密码验证】")
    private Integer signUpAuthType;

    @ApiModelProperty(value = "【报名验证值】")
    private String signUpAuthValue;


    public Integer getSignUpNum() {
        if (Optional.ofNullable(this.virtualStatus).orElse(0) == 1) {
            return Optional.ofNullable(this.signUpNum).orElse(0) + Optional.ofNullable(this.virtualSignUpNum).orElse(0);
        }
        return this.signUpNum;
    }
}
