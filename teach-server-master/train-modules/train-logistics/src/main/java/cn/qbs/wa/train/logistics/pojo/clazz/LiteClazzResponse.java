package cn.qbs.wa.train.logistics.pojo.clazz;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LiteClazzResponse {

    @ApiModelProperty(value = "班级ID")
    private Long id;

    @ApiModelProperty(value = "班级名称")
    private String clazzName;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "学员计划人数")
    private Integer studentNum;
}
