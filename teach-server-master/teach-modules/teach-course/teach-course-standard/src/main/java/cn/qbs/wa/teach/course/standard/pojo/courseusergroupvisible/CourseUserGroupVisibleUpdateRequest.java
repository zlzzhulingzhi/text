package cn.qbs.wa.teach.course.standard.pojo.courseusergroupvisible;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【课程可见学员分组】(CourseUserGroupVisible)更新【课程可见学员分组】参数
 *
 * @author makejava
 * @since 2022-05-09 16:08:00
 */
@Data
public class CourseUserGroupVisibleUpdateRequest {
    
    @ApiModelProperty(value = "【主键标识】")
    private Long id;
    
    @ApiModelProperty(value = "【组织机构ID】")
    private Long orgId;
    
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;
    
    @ApiModelProperty(value = "【分组ID】")
    private Long groupId;

}

