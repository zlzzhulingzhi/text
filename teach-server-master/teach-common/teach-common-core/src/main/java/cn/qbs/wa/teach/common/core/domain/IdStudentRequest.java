package cn.qbs.wa.teach.common.core.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IdStudentRequest {
    @ApiModelProperty(value = "学生id")
    private Long id;
}
