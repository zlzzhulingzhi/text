package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class StudentRelationDTO {

    @ApiModelProperty(value = "标签id")
    private List<Long> groupIds;

    @ApiModelProperty(value = "标签id")
    private List<Long> deptIds;
}
