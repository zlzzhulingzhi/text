package cn.qbs.wa.train.logistics.pojo.clazz;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

/**
 * 班级表(Clazz)创建班级表参数
 *
 * @author makejava
 * @since 2022-10-08 16:41:49
 */
@Data
public class ClazzAddRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "班级名称")
    @NotBlank(message = "班级名称不能为空")
    private String name;

    @ApiModelProperty(value = "课程ID")
    @NotNull(message = "课程不能为空")
    private Long courseId;

    @ApiModelProperty(value = "讲师ID")
    @NotNull(message = "讲师不能为空")
    private Long employeeId;

    @ApiModelProperty(value = "开班日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull
    private LocalDate startDate;

    @ApiModelProperty(value = "结班日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull
    private LocalDate endDate;

    @ApiModelProperty(value = "学员人数")
    private Integer studentNum;

    @ApiModelProperty(value = "课次数")
    private Integer lessonNum;

    @ApiModelProperty(value = "班级状态(0-待开班 1-开班 2-结班)")
    private Integer state;

    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private String plan;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "教室ID")
    private Long classroomId;

    @ApiModelProperty(value = "删除状态 0-正常 1-已删除")
    private Integer deleted;

}

