package cn.qbs.wa.train.logistics.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/12 14:38
 */
@Data
public class StudentData {

    // 用户名称 手机号码 性别 基础数据类. 这里的排序和excel里面的排序一致
    @ApiModelProperty(value = "行号")
    private Integer rowNum;

    @ApiModelProperty(value = "姓名")
    private String realName;

    //private String password;

    //private String email;
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "身份证")
    private String idNumber;

    @ApiModelProperty(value = "性别")
    private String sexName;

    @ApiModelProperty(value = "标签")
    private String groupName;

    @ApiModelProperty(value = "【性别 0-未知 1-男 2-女】")
    private Integer sex;

    @ApiModelProperty(value = "部门")
    private String deptName;
}
