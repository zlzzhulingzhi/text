package cn.qbs.wa.teach.course.standard.pojo.coursemessagereply;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【课程留言回复记录】(CourseMessageReply)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Data
public class CourseMessageReplyPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程留言ID】")
    private Long messageId;

    @ApiModelProperty(value = "【回复内容】")
    private String replyContent;

    @ApiModelProperty(value = "【回复目标ID】（reply：上一条回复ID; comment：reply_id = message_id）")
    private Long replyId;

    @ApiModelProperty(value = "【回复类型】（reply：针对回复; comment：针对评论）")
    private String replyType;

    @ApiModelProperty(value = "【回复用户昵称】")
    private String replyNickName;

    @ApiModelProperty(value = "【回复用户头像地址】")
    private String replyHeadImgUrl;

    @ApiModelProperty(value = "【交流人员ID】")
    private Long toUserId;

    @ApiModelProperty(value = "【点赞数】")
    private Integer praiseNum;

    @ApiModelProperty(value = "【举报】")
    private Integer reportStatus;

    @ApiModelProperty(value = "【删除状态 0 正常 1删除】")
    private Integer deleted;

}

