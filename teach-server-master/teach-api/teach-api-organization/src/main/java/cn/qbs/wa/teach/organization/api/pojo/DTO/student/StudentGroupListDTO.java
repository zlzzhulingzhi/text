package cn.qbs.wa.teach.organization.api.pojo.DTO.student;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学员(Student)分页查询学员响应
 *
 * @author WX
 * @since 2022-08-15 19:43:52
 */
@Data
public class StudentGroupListDTO {

    @ApiModelProperty(value = "【主键】")
    private Long groupId;

    @ApiModelProperty(value = "【标签名称】")
    private String groupName;

}

