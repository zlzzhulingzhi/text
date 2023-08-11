package cn.qbs.wa.train.logistics.excel;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/12 14:38
 */
@Data
public class EmployeeData {

    // 用户名称 手机号码 性别 基础数据类.这里的排序和excel里面的排序一致

    private String realName;

    //private String password;

    //private String email;

    private String phone;

    //private String idNumber;

    private String sexName;
}
