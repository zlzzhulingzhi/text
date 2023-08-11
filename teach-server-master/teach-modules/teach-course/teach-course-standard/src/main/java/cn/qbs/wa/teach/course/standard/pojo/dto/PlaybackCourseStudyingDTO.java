package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 直播观看记录
 *
 * @Author zcm
 * @Date 2022-06-23 17:40
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlaybackCourseStudyingDTO extends MyCourseStudyingDTO {

    @ApiModelProperty(value = "【观看记录id】")
    private Long id;

}

