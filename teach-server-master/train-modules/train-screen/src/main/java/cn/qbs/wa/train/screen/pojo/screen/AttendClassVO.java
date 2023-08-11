package cn.qbs.wa.train.screen.pojo.screen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author makejava
 * @since 2022-10-09 17:04:41
 */
@Data
public class AttendClassVO implements Serializable {

    @ApiModelProperty(value = "班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "班级名称")
    private String clazzName;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "教室")
    private String classroom;

    @ApiModelProperty(value = "学员人数")
    private Integer studentNum;

}
