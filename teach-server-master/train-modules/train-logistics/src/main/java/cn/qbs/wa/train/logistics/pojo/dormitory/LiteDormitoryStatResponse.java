package cn.qbs.wa.train.logistics.pojo.dormitory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LiteDormitoryStatResponse {

    @ApiModelProperty("未入住房间数")
    private Integer freeNum;

    @ApiModelProperty("已入住房间数")
    private Integer usedNum;

}
