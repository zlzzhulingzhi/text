package cn.qbs.wa.train.logistics.pojo.clazzlesson;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author makejava
 * @since 2023-03-14 09:21:36
 */
@Data
public class LessonResponse {

    @ApiModelProperty(value = "内容名")
    private String contentName;

    @ApiModelProperty(value = "教室编号")
    private String roomNo;

    @ApiModelProperty(value = "讲师姓名")
    private String lecturerName;

    @ApiModelProperty(value = "上课时间")
    private String lessonTime;

    @ApiModelProperty(value = "课程名称")
    private String lessonName;

    @ApiModelProperty(value = "内容描述")
    private String contentDescription;

}
