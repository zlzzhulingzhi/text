package cn.qbs.wa.train.logistics.pojo.student;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学员-用户id参数
 *
 * @author makejava
 * @since 2022-03-28 16:07:15
 */
@Data
public class StudentIdAndUserIdRequest {

    @ApiModelProperty(value = "学员id")
    private Long studentId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

}

