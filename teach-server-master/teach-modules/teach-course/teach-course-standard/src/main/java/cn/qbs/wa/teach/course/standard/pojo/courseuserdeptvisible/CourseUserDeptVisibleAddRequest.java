package cn.qbs.wa.teach.course.standard.pojo.courseuserdeptvisible;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【课程可见学员部门】(CourseUserDeptVisible)创建【课程可见学员部门】参数
 *
 * @author makejava
 * @since 2022-05-09 16:07:08
 */
@Data
public class CourseUserDeptVisibleAddRequest {
    
    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;
    
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;
    
    @ApiModelProperty(value = "【部门ID】")
    private Long deptId;

}

