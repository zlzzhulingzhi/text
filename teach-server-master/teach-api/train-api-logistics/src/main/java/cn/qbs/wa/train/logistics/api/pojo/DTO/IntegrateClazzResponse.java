package cn.qbs.wa.train.logistics.api.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IntegrateClazzResponse {

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课时名称")
    private String contentName;

    @ApiModelProperty(value = "班级名称")
    private String clazzName;

    @ApiModelProperty(value = "开班日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "结班日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "班级状态(0-待开班 1-开班 2-结班)")
    private Integer state;

    @ApiModelProperty(value = "教室编号")
    private String roomNo;
}
