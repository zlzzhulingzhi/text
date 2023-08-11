package cn.qbs.wa.teach.organization.api.pojo.DTO.student;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学员(Student)学员详情
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Data
public class StudentDetailResponseDTO {

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【电话号码】")
    private String phone;

}

