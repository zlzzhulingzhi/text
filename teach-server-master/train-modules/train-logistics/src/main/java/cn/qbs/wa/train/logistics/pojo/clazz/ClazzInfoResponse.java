package cn.qbs.wa.train.logistics.pojo.clazz;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ClazzInfoResponse {

    @ApiModelProperty(value = "班级主键")
    private Long id;

    @ApiModelProperty(value = "班级名称")
    private String name;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "开班日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    @ApiModelProperty(value = "结班日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

    @ApiModelProperty(value = "计划课次")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程讲师")
    private List<String> lecturers;

    @ApiModelProperty(value = "员工ID")
    private Long employeeId;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "班级状态(0-待开班 1-开班 2-结班)")
    private Integer state;

    @ApiModelProperty(value = "学员计划人数")
    private Integer studentNum;

    @ApiModelProperty(value = "学员实际人数")
    private Integer studentCount;

}
