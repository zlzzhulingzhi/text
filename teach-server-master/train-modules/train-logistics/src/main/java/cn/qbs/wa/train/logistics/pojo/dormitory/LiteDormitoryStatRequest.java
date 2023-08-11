package cn.qbs.wa.train.logistics.pojo.dormitory;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LiteDormitoryStatRequest {

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate useDate;

    @ApiModelProperty(value = "单元(字典值)")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    private String floor;

    @ApiModelProperty(value = "房型(字典值)")
    private String roomType;
}
