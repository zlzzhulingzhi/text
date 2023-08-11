package cn.qbs.wa.teach.course.standard.pojo.coursemessage;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【课程留言】(CourseMessage)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Data
public class CourseMessagePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @ApiModelProperty(value = "【留言】")
    private String message;

    @ApiModelProperty(value = "【留言用户昵称】")
    private String userNickName;

    @ApiModelProperty(value = "【留言用户头像地址】")
    private String userHeadImgUrl;

    @ApiModelProperty(value = "【回复状态 0 未回复 1 已回复】")
    private Integer replyStatus;

    @ApiModelProperty(value = "【点赞数】")
    private Integer praiseNum;

    @ApiModelProperty(value = "【举报】")
    private Integer reportStatus;

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

}

