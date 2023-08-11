package cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class LecturerSearchDTO {

    @ApiModelProperty(value = "讲师ID")
    private List<Long> idList;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "用户id")
    private Long userId;


}
