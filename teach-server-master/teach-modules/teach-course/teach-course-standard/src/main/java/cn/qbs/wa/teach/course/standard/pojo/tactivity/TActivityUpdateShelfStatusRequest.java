package cn.qbs.wa.teach.course.standard.pojo.tactivity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动表(TActivity)更新活动表参数
 *
 * @author makejava
 * @since 2022-12-13 15:55:04
 */
@Data
public class TActivityUpdateShelfStatusRequest {

    @ApiModelProperty(value = "主键")
    private List<Long> ids;

    @ApiModelProperty(value = "上架状态 0下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "上架时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime shelfTime;
}

