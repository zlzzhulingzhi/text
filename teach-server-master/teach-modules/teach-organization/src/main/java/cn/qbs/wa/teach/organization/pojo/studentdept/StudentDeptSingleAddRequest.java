package cn.qbs.wa.teach.organization.pojo.studentdept;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学员部门表(StudentDept)创建学员部门表参数
 *
 * @author makejava
 * @since 2022-05-09 15:15:32
 */
@Data
public class StudentDeptSingleAddRequest {

    @ApiModelProperty(value = "学员id")
    private Long studentId;;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

}

