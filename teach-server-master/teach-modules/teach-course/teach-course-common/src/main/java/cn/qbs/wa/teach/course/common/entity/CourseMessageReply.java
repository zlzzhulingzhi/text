package cn.qbs.wa.teach.course.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class CourseMessageReply extends Model {

    private static final long serialVersionUID = 688417532683818898L;


    @ApiModelProperty(value = "【主键标识】")
    private Long id;

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

    @TableLogic
    @ApiModelProperty(value = "【删除状态 0-正常 1-删除】")
    private Integer deleted;

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
