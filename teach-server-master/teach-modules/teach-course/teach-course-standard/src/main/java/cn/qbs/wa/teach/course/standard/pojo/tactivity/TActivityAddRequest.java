package cn.qbs.wa.teach.course.standard.pojo.tactivity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 活动表(TActivity)创建活动表参数
 *
 * @author makejava
 * @since 2022-12-13 15:55:04
 */
@Data
public class TActivityAddRequest {

    @ApiModelProperty(value = "活动名称")
    private String title;

    @ApiModelProperty(value = "活动封面")
    private String coverUrl;

    @ApiModelProperty(value = "活动介绍")
    private String introduction;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "活动时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime activityTime;

}

