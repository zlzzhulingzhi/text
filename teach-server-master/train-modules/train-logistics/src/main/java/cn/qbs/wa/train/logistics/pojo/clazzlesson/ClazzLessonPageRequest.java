package cn.qbs.wa.train.logistics.pojo.clazzlesson;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 班级课程(ClazzLesson)分页查询参数
 *
 * @author makejava
 * @since 2023-03-13 20:12:57
 */
@Data
public class ClazzLessonPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "课程名称")
    private String lessonName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}

