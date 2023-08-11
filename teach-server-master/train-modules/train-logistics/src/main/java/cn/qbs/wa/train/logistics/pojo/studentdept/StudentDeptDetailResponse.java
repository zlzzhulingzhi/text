package cn.qbs.wa.train.logistics.pojo.studentdept;

import cn.qbs.wa.train.logistics.entity.StudentDept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

