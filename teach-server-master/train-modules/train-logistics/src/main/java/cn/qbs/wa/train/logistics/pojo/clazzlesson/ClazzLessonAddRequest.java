package cn.qbs.wa.train.logistics.pojo.clazzlesson;


import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 班级课程(ClazzLesson)创建班级课程参数
 *
 * @author makejava
 * @since 2023-03-13 20:12:57
 */
@Data
public class ClazzLessonAddRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "课程名称")
    private String lessonName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    List<ClazzLessonArrange> clazzLessonArrangeList;

}

