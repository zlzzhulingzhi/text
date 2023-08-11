package cn.qbs.wa.train.logistics.pojo.clazz;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ClazzSummaryResponse {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "培训班数量")
    private Long clazzCount;

    @ApiModelProperty(value = "学生数量")
    private Long studentCount;

    @ApiModelProperty(value = "总学时")
    private Long lessonNumCount;

    @ApiModelProperty(value = "培训班总数量")
    private Long totalClazzCount;

    @ApiModelProperty(value = "学生总数量")
    private Long totalStudentCount;

}

