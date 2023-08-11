package cn.qbs.wa.train.basic.pojo.unit;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 单位表(Unit)更新单位表参数
 *
 * @author makejava
 * @since 2022-09-29 08:31:22
 */
@Data
public class UnitUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "单位名称")
    @NotBlank(message = "单位名称不能为空")
    private String name;

    @ApiModelProperty(value = "简称")
    private String briefName;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "信用代码")
    private String creditCode;

    @ApiModelProperty(value = "单位性质")
    private Integer type;

    @ApiModelProperty(value = "联系人")
    private String contactPerson;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "启用状态 0-禁用 1-启用")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

}

