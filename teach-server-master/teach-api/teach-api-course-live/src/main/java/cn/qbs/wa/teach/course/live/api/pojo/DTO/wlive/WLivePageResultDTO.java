package cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 机构插件表(WOrg)分页查询机构插件表响应
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
@Data
public class WLivePageResultDTO {



    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "直播名称")
    private String liveName;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "封面地址")
    private String coverUrl;

    @ApiModelProperty(value = "业务类型")
    private Integer businessType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

}

