package cn.qbs.wa.train.logistics.pojo.clazz;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 班级表(Clazz)分页查询参数
 *
 * @author makejava
 * @since 2022-10-08 16:41:49
 */
@Data
public class ClazzListRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "班级名称")
    private String name;

    @ApiModelProperty(value = "课程ID")
    private Long courseId;

    @ApiModelProperty(value = "班级状态(0-待开班 1-开班 2-结班)")
    private String state;

    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private String plan;

}

