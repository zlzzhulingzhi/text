package cn.qbs.wa.train.logistics.pojo.clazzlessonarrange;


import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.LessonResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClazzLessonArrangeListResponse extends ClazzLessonArrange {


    @ApiModelProperty(value = "教室编号")
    private String roomNo;

    @ApiModelProperty(value = "教室类别(字典值)")
    private String roomType;

    @ApiModelProperty(value = "宿舍单元(字典值)")
    private String building;

    @ApiModelProperty(value = "课程名称")
    private String lessonName;

    @ApiModelProperty(value = "上课日期")
    private String lessonDate;

    @ApiModelProperty(value = "上课时间")
    private String lessonTime;

    private List<String> lessonNameList;

    private List<Long> lessonIdList;

    private List<LessonResponse> lessonList;

}
