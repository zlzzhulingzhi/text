package cn.qbs.wa.train.logistics.pojo.unitstaff;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 单位职员表(UnitStaff)更新单位职员表参数
 *
 * @author makejava
 * @since 2022-09-29 09:04:03
 */
@Data
public class UnitStaffUpdateBatchRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户状态 0-不可用  1-可用")
    private String enabled;

}

