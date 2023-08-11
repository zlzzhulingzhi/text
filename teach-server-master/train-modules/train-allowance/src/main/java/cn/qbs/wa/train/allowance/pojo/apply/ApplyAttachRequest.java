package cn.qbs.wa.train.allowance.pojo.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyAttachRequest {

    @ApiModelProperty(value = "附件代号 规则：申请名称拼音首字母大写 - 机构(O)/平台(P) - 文件序号(3位) 如：资质申请机构申请附件 - ZZSQ-O-001")
    private String attachCode;

    @ApiModelProperty(value = "附件名称")
    private String attachName;

    @ApiModelProperty(value = "附件地址")
    private String attachUrl;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

}
