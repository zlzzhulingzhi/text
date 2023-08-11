package cn.qbs.wa.teach.course.standard.pojo.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReportQo {

    @ApiModelProperty(value = "页面类型，1-课程，2-微页, 3-砍价详情，4-拼团详情，5-校区", example = "1")
    Integer type;

    @ApiModelProperty(value = "对象ID,根据type类型传参，1,3,4-课程ID, 2-微页ID,5-校区ID", example = "1")
    Long id;

    @ApiModelProperty(value = "跳转路径", example = "page/xxxx")
    String page;

    @ApiModelProperty(value = "跳转参数", example = "111")
    String scene;

    /*@ApiModelProperty(value = "机构Id")
    Long orgId;*/

    @ApiModelProperty(value = "机构Id")
    Long memberId;
}
