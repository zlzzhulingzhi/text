package cn.qbs.wa.union.admin.pojo.unimember;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 统一会员用户表(UniMember)创建统一会员用户表参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-07-21 09:11:24
 */
@Data
public class UniMemberTCourseStudentRequest {

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "姓名")
    private String realName;


}

