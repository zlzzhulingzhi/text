package cn.qbs.wa.teach.course.standard.pojo.dto.app;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【课程评价】(CourseComment)分页查询参数
 *
 * @author yjx
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageCommentDTO extends BasePageRequest {

    @ApiModelProperty(value = "【机构ID】")
    private Long orgId;

    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

}

