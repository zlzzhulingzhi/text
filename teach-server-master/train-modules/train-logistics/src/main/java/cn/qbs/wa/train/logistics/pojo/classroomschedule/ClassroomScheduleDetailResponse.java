package cn.qbs.wa.train.logistics.pojo.classroomschedule;

import cn.qbs.wa.train.logistics.entity.ClassroomSchedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 教室日程表(ClassroomSchedule)教室日程表详情
 *
 * @author makejava
 * @since 2022-10-18 11:23:15
 */
@Data
public class ClassroomScheduleDetailResponse extends ClassroomSchedule {
    @ApiModelProperty(value = "机构名称")
    private String orgName;
}
