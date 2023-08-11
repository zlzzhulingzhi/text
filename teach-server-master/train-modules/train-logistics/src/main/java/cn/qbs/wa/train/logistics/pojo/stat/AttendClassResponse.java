package cn.qbs.wa.train.logistics.pojo.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class AttendClassResponse implements Serializable {

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "班级名称")
    private String clazzName;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "教室")
    private String classroom;

    @ApiModelProperty(value = "学员人数")
    private Integer studentNum;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开班日期")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结班日期")
    private LocalDate endDate;

}