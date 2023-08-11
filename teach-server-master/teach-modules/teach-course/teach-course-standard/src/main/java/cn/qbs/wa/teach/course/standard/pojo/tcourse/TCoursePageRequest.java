package cn.qbs.wa.teach.course.standard.pojo.tcourse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 课程-预报名学员(TCourseStudent)分页查询参数
 *
 * @author makejava
 * @since 2022-10-27 14:45:56
 */
@Data
public class TCoursePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程id")
    private List<Long> courseIdList;

    @ApiModelProperty(value = "主键")
    private List<Long> idList;
}

