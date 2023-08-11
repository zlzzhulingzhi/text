package cn.qbs.wa.train.logistics.pojo.dormitoryschedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

/**
 * 宿舍排期表(DormitorySchedule)分页查询参数
 *
 * @author makejava
 * @since 2022-10-18 14:10:54
 */
@Data
public class DormitoryStateRequest{

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull
    private LocalDate startDate;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull
    private  LocalDate endDate;

    @ApiModelProperty(value = "宿舍单元(字典值)")
    private String building;

    @ApiModelProperty(value = "楼层(字典值)")
    private String floor;

    @ApiModelProperty(value = "房号")
    private String roomNo;

    @ApiModelProperty(value = "房型(字典值)")
    private String roomType;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    private Integer current;

    private Integer size;

}

