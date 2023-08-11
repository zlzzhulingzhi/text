package cn.qbs.wa.train.logistics.pojo.clazzlesson;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 班级课程(ClazzLesson)更新班级课程参数
 *
 * @author makejava
 * @since 2023-03-13 20:12:57
 */
@Data
public class ClazzLessonUpdateRequest {

    @ApiModelProperty(value = "主键标识")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "课程名称")
    private String lessonName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "【排序列表】")
    private List<Long> sortedIds;

}

