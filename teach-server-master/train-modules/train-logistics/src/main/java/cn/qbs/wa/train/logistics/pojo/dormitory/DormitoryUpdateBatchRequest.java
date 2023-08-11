package cn.qbs.wa.train.logistics.pojo.dormitory;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 教室表(Classroom)更新教室表参数
 *
 * @author makejava
 * @since 2022-10-11 17:30:11
 */
@Data
public class DormitoryUpdateBatchRequest {

    @ApiModelProperty(value = "主键")
    private List<Long> ids;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

}

