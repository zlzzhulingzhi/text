package cn.qbs.wa.train.logistics.pojo.clazz;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 班级表(Clazz)分页查询班级表响应
 *
 * @author makejava
 * @since 2022-10-08 16:41:49
 */
@Data
public class ClazzListResponse {

    @ApiModelProperty(value = "班级主键")
    private Long id;

    @ApiModelProperty(value = "班级名称")
    private String name;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "【课程名称】")
    private String courseName;

    @ApiModelProperty(value = "员工ID")
    private Long employeeId;

    @ApiModelProperty(value = "员工姓名")
    private String employeeName;

    @ApiModelProperty(value = "班级状态(0-待开班 1-开班 2-结班)")
    private Integer state;

    private LocalDate startDate;

    private Integer year;

    public Integer getYear() {
        if (startDate != null) {
            return startDate.getYear();
        }
        return year;
    }

}

