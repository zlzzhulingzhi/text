package cn.qbs.wa.teach.course.common.entity.live;


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
 * @since 2021-12-30 13:37:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicLiveInfo extends Model {

    private static final long serialVersionUID = 982956791615927066L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;

    @ApiModelProperty(value = "简介")
    private String introduction;

    @ApiModelProperty(value = "分享二维码地址")
    private String qrcodeUrl;

    @ApiModelProperty(value = "点赞人数")
    private Integer likeCount;

    @ApiModelProperty(value = "观众人数(不包括未直播就进入的人)")
    private Integer viewerCount;

    @ApiModelProperty(value = "0 不能回放 1可以回放")
    private Integer playbacked;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;



}
