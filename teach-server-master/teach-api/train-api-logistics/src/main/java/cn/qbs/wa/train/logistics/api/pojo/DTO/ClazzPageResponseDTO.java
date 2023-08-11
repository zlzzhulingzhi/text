package cn.qbs.wa.train.logistics.api.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClazzPageResponseDTO {

    @ApiModelProperty(value = "课程ID")
    private Long courseId;
}

