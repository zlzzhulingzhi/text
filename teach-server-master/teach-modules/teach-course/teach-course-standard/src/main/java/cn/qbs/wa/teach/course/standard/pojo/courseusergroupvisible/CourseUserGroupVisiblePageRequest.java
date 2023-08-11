package cn.qbs.wa.teach.course.standard.pojo.courseusergroupvisible;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【课程可见学员分组】(CourseUserGroupVisible)分页查询参数
 *
 * @author makejava
 * @since 2022-05-09 16:07:59
 */
@Data
public class CourseUserGroupVisiblePageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;
    
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;
    
    @ApiModelProperty(value = "【分组ID】")
    private Long groupId;

}

