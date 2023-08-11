package cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 讲师表(Lecturer)讲师表详情
 *
 * @author makejava
 * @since 2021-11-17 11:25:34
 */
@Data
public class LecturerDetailResponseDTO {

    @ApiModelProperty(value = "讲师姓名")
    private String lecturerName;

}

