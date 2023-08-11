package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyAttachResponse {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "附件代号")
    private String attachCode;

    @ApiModelProperty(value = "附件名称")
    private String attachName;

    @ApiModelProperty(value = "附件地址")
    private String attachUrl;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

}
