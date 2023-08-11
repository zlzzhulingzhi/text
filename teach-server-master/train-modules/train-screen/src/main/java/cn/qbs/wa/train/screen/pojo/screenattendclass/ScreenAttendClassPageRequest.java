package cn.qbs.wa.train.screen.pojo.screenattendclass;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 大屏-今日培训班级(ScreenAttendClass)分页查询参数
 *
 * @author makejava
 * @since 2022-10-14 14:49:06
 */
@Data
public class ScreenAttendClassPageRequest extends BasePageRequest {

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

