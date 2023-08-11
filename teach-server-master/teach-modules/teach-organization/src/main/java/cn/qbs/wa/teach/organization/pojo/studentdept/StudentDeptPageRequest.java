package cn.qbs.wa.teach.organization.pojo.studentdept;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 学员部门表(StudentDept)分页查询参数
 *
 * @author makejava
 * @since 2022-05-09 15:15:30
 */
@Data
public class StudentDeptPageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "学员id")
    private Long studentId;
    
    @ApiModelProperty(value = "部门id")
    private Long deptId;

}

