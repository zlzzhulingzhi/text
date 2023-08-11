package cn.qbs.wa.teach.organization.pojo.studentdept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.organization.entity.StudentDept;

/**
 * 学员部门表(StudentDept)学员部门表详情
 *
 * @author makejava
 * @since 2022-05-09 15:15:32
 */
@Data
public class StudentDeptDetailResponse extends StudentDept {

    @ApiModelProperty(value = "部门名称")
    private String deptName;
}

