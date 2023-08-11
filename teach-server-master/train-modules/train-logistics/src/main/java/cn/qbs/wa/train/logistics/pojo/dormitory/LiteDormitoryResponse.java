package cn.qbs.wa.train.logistics.pojo.dormitory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LiteDormitoryResponse {

    @ApiModelProperty(value = "宿舍ID")
    private Long dormitoryId;

    @ApiModelProperty(value = "单元(字典值)")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    private String floor;

    @ApiModelProperty(value = "教室类别(字典值)")
    private String roomType;

    @ApiModelProperty(value = "教室编号")
    private String roomNo;

    @ApiModelProperty(value = "当前预定情况 1-已预订 0-空闲")
    private Integer scheduleNow;

    @ApiModelProperty(value = "当前预定机构")
    private String orgName;

    @ApiModelProperty(value = "后续预定情况 1-有 0-无")
    private Integer scheduleAfter;

}
