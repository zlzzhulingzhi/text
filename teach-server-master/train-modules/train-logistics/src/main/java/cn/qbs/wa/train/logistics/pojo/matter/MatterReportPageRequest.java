package cn.qbs.wa.train.logistics.pojo.matter;

import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class MatterReportPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "类别(1-开课、2-维修、3-更换、4-结课)")
    private Integer category;

    @ApiModelProperty(value = "上报日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate reportDate;

    @ApiModelProperty(value = "关联教室")
    private Long classroomId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "教室编号")
    private String roomNo;
}
