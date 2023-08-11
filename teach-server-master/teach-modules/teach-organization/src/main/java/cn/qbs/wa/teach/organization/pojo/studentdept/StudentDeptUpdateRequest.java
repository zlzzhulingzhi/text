package cn.qbs.wa.teach.organization.pojo.studentdept;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 学员部门表(StudentDept)更新学员部门表参数
 *
 * @author makejava
 * @since 2022-05-09 15:15:32
 */
@Data
public class StudentDeptUpdateRequest {
    
    @ApiModelProperty(value = "")
    private Long id;
    
    @ApiModelProperty(value = "学员id")
    private Long studentId;
    
    @ApiModelProperty(value = "部门id")
    private Long deptId;

}

