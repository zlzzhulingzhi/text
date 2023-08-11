package cn.qbs.wa.train.logistics.pojo.dormitoryschedule;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * 宿舍排期表(DormitorySchedule)创建宿舍排期表参数
 *
 * @author makejava
 * @since 2022-10-18 14:10:54
 */
@Data
public class UseDateState {

    @ApiModelProperty(value = "使用状态(0-未使用，1-已使用)")
    private String useState;

    @ApiModelProperty(value = "使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate useDate;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

}

