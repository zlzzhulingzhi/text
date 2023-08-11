package cn.qbs.wa.teach.course.standard.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yjx
 */
@Data
public class CourseCommentViewDTO {

    private Long id;

    @ApiModelProperty(value = "【评价】")
    private String content;

    @ApiModelProperty(value = "【评分】")
    private BigDecimal score;

    @ApiModelProperty(value = "【留言用户昵称】")
    private String userNickName;

    @ApiModelProperty(value = "【留言用户头像地址】")
    private String userHeadImgUrl;

    @ApiModelProperty(value = "【创建时间】")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;
}
