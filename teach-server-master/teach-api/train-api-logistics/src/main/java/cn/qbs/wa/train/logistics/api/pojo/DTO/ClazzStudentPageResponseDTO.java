package cn.qbs.wa.train.logistics.api.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClazzStudentPageResponseDTO {

    @ApiModelProperty(value = "学员ID")
    private Long studentId;

    @ApiModelProperty(value = "学员用户ID")
    private Long memberId;

    @ApiModelProperty(value = "班级名称")
    private String clazzName;
}

