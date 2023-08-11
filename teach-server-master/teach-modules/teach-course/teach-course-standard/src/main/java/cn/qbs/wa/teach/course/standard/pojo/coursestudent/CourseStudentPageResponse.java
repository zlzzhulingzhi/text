package cn.qbs.wa.teach.course.standard.pojo.coursestudent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import java.util.List;

/**
 * 【课程学员】(CourseStudent)分页查询【课程学员】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Data
public class CourseStudentPageResponse extends CourseStudent {

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "【账号状态】")
    private String enabled;

    @ApiModelProperty(value = "【部门名称】")
    private String deptName;

    @ApiModelProperty(value = "标签集合")
    private String groupNames;
}

