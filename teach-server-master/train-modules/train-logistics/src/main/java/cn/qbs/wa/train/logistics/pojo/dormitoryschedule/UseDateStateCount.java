package cn.qbs.wa.train.logistics.pojo.dormitoryschedule;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 宿舍排期表(DormitorySchedule)创建宿舍排期表参数
 *
 * @author makejava
 * @since 2022-10-18 14:10:54
 */
@Data
public class UseDateStateCount {

    @ApiModelProperty(value = "房型(字典值)")
    private String roomType;

    @ApiModelProperty(value = "空闲")
    private Integer freeNum;

    @ApiModelProperty(value = "入驻")
    private Integer useNum;

}

