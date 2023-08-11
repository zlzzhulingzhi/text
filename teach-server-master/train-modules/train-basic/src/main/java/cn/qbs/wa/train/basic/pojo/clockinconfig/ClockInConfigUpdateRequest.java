package cn.qbs.wa.train.basic.pojo.clockinconfig;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 打卡配置(ClockInConfig)更新打卡配置参数
 *
 * @author makejava
 * @since 2022-12-26 15:38:26
 */
@Data
public class ClockInConfigUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "编号")
    private String siteCode;

    @ApiModelProperty(value = "打卡地点")
    private String siteName;

    @ApiModelProperty(value = "经度坐标")
    private String longitude;

    @ApiModelProperty(value = "纬度坐标")
    private String latitude;

    @ApiModelProperty(value = "打卡距离(m)")
    private Integer distance;

    @ApiModelProperty(value = "备注")
    private String remark;

}

