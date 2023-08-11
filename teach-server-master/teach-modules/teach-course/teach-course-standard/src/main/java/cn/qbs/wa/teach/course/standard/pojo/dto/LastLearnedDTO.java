package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LastLearnedDTO {
    @ApiModelProperty(value = "【课程章节名】")
    private String chapterName;

    @ApiModelProperty(value = "【讲次名】")
    private String lessonName;

}
