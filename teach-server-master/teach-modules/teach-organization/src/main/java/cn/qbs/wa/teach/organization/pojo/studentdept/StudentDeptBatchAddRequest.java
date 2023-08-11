package cn.qbs.wa.teach.organization.pojo.studentdept;


import cn.qbs.wa.teach.organization.pojo.student.StudentIdAndUserIdRequest;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 学员部门表(StudentDept)创建学员部门表参数
 *
 * @author makejava
 * @since 2022-05-09 15:15:32
 */
@Data
public class StudentDeptBatchAddRequest {

    @ApiModelProperty(value = "学员id集合")
    private List<StudentIdAndUserIdRequest> studentIdList;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

}

