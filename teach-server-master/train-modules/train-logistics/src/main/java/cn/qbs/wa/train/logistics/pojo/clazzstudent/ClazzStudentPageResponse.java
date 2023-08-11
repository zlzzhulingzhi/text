package cn.qbs.wa.train.logistics.pojo.clazzstudent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.logistics.entity.ClazzStudent;

/**
 * 班级学员表(ClazzStudent)分页查询班级学员表响应
 *
 * @author makejava
 * @since 2022-10-08 17:28:15
 */
@Data
public class ClazzStudentPageResponse extends ClazzStudent {

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "班级名称")
    private String clazzName;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "【电话号码】")
    private String phone;

    @ApiModelProperty(value = "班级学生id")
    private Long clazzStudentId;
}

