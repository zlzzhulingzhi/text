package cn.qbs.wa.train.logistics.pojo.dormitory;

import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomScheduleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.DormitoryScheduleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.UseDateState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.logistics.entity.Dormitory;
import java.util.List;

/**
 * 宿舍表(Dormitory)分页查询宿舍表响应
 *
 * @author makejava
 * @since 2022-10-08 17:40:00
 */
@Data
public class DormitoryPageResponse extends Dormitory {

    @ApiModelProperty(value = "日期使用状态列表")
    private List<UseDateState> useDateStateList;

    @ApiModelProperty(value = "教室日程情况")
    List<DormitoryScheduleDetailResponse> dormitoryScheduleDetailResponses;

}