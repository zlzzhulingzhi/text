package cn.qbs.wa.train.logistics.pojo.importemployee;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 导入职工表(ImportEmployee)分页查询参数
 *
 * @author makejava
 * @since 2021-11-16 11:29:44
 */
@Data
public class ImportEmployeePageRequest extends BasePageRequest {

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

