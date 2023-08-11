package cn.qbs.wa.train.logistics.pojo.clazz;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.util.Date;
import java.util.List;

/**
 * 班级表(Clazz)分页查询参数
 *
 * @author makejava
 * @since 2022-10-08 16:41:49
 */
@Data
public class ClazzPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "班级名称")
    private String name;

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "讲师ID")
    private Long employeeId;

    @ApiModelProperty(value = "开班日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value = "结班日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "学员人数")
    private Integer studentNum;

    @ApiModelProperty(value = "课次数")
    private Integer lessonNum;

    @ApiModelProperty(value = "班级状态(0-待开班 1-开班 2-结班)")
    private String state;

    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private String plan;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "删除状态 0-正常 1-已删除")
    private Integer deleted;

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程id列表")
    private List<Long> courseIdList;

    @ApiModelProperty(value = "班级id列表")
    private List<Long> idList;

    @ApiModelProperty(value = "讲师名称")
    private String lecturerName;

}

