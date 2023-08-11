package cn.qbs.wa.train.logistics.pojo.clazzstudent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.logistics.entity.ClazzStudent;

/**
 * 班级学员表(ClazzStudent)班级学员表详情
 *
 * @author makejava
 * @since 2022-10-08 17:28:15
 */
@Data
public class ClazzStudentDetailResponse extends ClazzStudent {

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "班级名称")
    private String clazzName;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

}

