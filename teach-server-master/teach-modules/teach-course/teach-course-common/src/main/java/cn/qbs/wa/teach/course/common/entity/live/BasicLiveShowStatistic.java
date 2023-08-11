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
 * @since 2021-12-29 11:07:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicLiveShowStatistic extends Model {

    private static final long serialVersionUID = 937584419193879403L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "观众人数(不包括未直播就进入的人)")
    private Integer viewerCount;

    @ApiModelProperty(value = "累计在线人数(包括未播放就进入的人)")
    private String uniqueVisitor;

    @ApiModelProperty(value = "累计在线人次 pv")
    private Integer pageView;

    @ApiModelProperty(value = "在线总时长")
    private Integer totalOnlineDuration;

    @ApiModelProperty(value = "观看总时长")
    private String totalViewingDuration;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "直播场次id")
    private Long basicLiveShowId;

}
