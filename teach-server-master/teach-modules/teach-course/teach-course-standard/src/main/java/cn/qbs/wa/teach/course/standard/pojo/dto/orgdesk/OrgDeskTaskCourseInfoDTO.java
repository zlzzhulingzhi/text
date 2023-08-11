package cn.qbs.wa.teach.course.standard.pojo.dto.orgdesk;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 机构前台h5任务详情中包含的课程
 *
 * @author WX
 * @version 1.0
 * @date 2022-03-15 14:13:18
 */
@Data
public class OrgDeskTaskCourseInfoDTO {

    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程封面")
    private String courseCoverUrl;

    @ApiModelProperty(value = "【学习完成率】")
    private Integer courseRateProgress;

    @ApiModelProperty(value = "【上次学到的章节名称】")
    private String lastLearned;

}
