package cn.qbs.wa.teach.organization.pojo.importemployee;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 导入职工表(ImportEmployee)创建导入职工表参数
 *
 * @author makejava
 * @since 2021-11-16 11:29:44
 */
@Data
public class ImportEmployeeAddRequest {

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

    @ApiModelProperty(value = "性别 0-未知 1-男 2-女")
    private Integer sex;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "事件id")
    private String eventId;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "")
    private String idNumber;

}

