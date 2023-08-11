package cn.qbs.wa.teach.course.standard.pojo.dto.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author yjx
 */
@Data
public class PageCourseVO {

    @ApiModelProperty(value = "【课程ID】")
    private Long id;

    @ApiModelProperty(value = "【课程类型 live：直播、record：录播、mix：综合】")
    private String courseType;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "【课程封面】")
    private String coverUrl;

    @ApiModelProperty(value = "【评分】")
    private BigDecimal score;

    @ApiModelProperty(value = "报名人数")
    private Integer signUpNum;

    @ApiModelProperty(value = "【课时数】")
    private Integer lessonNum;

    @ApiModelProperty(value = "【课程积分】")
    private Integer coursePoints;

    @ApiModelProperty(value = "【上架时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime shelfTime;

    @ApiModelProperty(value = "【课程价格 单位分】")
    private Long coursePrice;

    @ApiModelProperty(value = "【精品课程 1-精品课程 0-普通课程(默认)】")
    private Integer gooded;

    @ApiModelProperty(value = "【虚拟报名人数状态 0-关闭 1-开启】")
    private Integer virtualStatus;

    @ApiModelProperty(value = "【虚拟报名人数状态 0-关闭 1-开启】")
    private Integer virtualSignUpNum;

    public Integer getSignUpNum() {
        if (Optional.ofNullable(this.virtualStatus).orElse(0) == 1) {
            return Optional.ofNullable(this.signUpNum).orElse(0) + Optional.ofNullable(this.virtualSignUpNum).orElse(0);
        }
        return this.signUpNum;
    }
}
