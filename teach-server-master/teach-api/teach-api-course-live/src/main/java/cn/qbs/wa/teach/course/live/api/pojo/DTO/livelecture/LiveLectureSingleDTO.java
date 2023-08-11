package cn.qbs.wa.teach.course.live.api.pojo.DTO.livelecture;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/11 14:48
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LiveLectureSingleDTO {

    @ApiModelProperty(value = "讲师id")
    private Long lectureId;

    @ApiModelProperty(value = "用户id")
    private Long userId;
}
