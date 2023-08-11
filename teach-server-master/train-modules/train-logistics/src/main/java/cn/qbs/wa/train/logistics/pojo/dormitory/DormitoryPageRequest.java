package cn.qbs.wa.train.logistics.pojo.dormitory;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.time.LocalDate;

/**
 * 宿舍表(Dormitory)分页查询参数
 *
 * @author makejava
 * @since 2022-10-08 17:40:00
 */
@Data
public class DormitoryPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "宿舍单元(字典值)")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    private String floor;

    @ApiModelProperty(value = "房号")
    private String roomNo;

    @ApiModelProperty(value = "房型(字典值)")
    private String roomType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

    @ApiModelProperty(value = "删除状态 0-正常 1-已删除")
    private Integer deleted;

    @ApiModelProperty(value = "使用状态(0-未使用，1-已使用)")
    private Integer useState;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

}

