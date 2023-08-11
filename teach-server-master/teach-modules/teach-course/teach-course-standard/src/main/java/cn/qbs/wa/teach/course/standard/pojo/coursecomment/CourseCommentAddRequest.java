package cn.qbs.wa.teach.course.standard.pojo.coursecomment;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 【课程评价】(CourseComment)创建【课程评价】参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-24 11:05:15
 */
@Data
public class CourseCommentAddRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【评价】")
    private String content;

    @DecimalMin(value = "0", message = "评分必须大于等于0分")
    @DecimalMax(value = "5", message = "评分必须小于等于5分")
    @ApiModelProperty(value = "【评分】")
    private BigDecimal score = new BigDecimal("5");

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

    @ApiModelProperty(value = "【留言用户昵称】")
    private String userNickName;

    @ApiModelProperty(value = "【留言用户头像地址】")
    private String userHeadImgUrl;

}

