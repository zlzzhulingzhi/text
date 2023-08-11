package cn.qbs.wa.train.logistics.pojo.dormitoryschedule;

import cn.qbs.wa.train.logistics.entity.DormitorySchedule;
import cn.qbs.wa.train.logistics.pojo.dormitory.DormitoryPageResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 宿舍排期表(DormitorySchedule)分页查询宿舍排期表响应
 *
 * @author makejava
 * @since 2022-10-18 14:10:55
 */
@Data
public class DormitoryStateResponse{

    @ApiModelProperty(value = "宿舍列表")
    private List<DormitoryPageResponse> dormitoryPageResponseList;

    private Integer total;

    @ApiModelProperty(value = "统计列表")
    private List<UseDateStateCount> useDateStateCountList;

}

