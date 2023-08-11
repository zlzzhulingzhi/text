package cn.qbs.wa.teach.course.standard.pojo.courseuserdeptvisible;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 【课程可见学员部门】(CourseUserDeptVisible)分页查询参数
 *
 * @author makejava
 * @since 2022-05-09 16:06:54
 */
@Data
public class CourseUserDeptVisiblePageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;
    
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;
    
    @ApiModelProperty(value = "【部门ID】")
    private Long deptId;

}

