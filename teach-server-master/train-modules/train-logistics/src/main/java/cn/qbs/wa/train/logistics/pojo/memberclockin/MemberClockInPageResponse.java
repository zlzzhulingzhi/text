package cn.qbs.wa.train.logistics.pojo.memberclockin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.train.logistics.entity.MemberClockIn;

/**
 * 学员打卡记录(MemberClockIn)分页查询学员打卡记录响应
 *
 * @author makejava
 * @since 2022-12-26 15:42:24
 */
@Data
public class MemberClockInPageResponse extends MemberClockIn {

    @ApiModelProperty(value = "名称")
    String realName;

    @ApiModelProperty(value = "【手机号码】")
    private String phone;

}

