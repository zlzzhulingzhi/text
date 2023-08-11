package cn.qbs.wa.train.logistics.pojo.memberclockin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 学员打卡记录(MemberClockIn)创建学员打卡记录参数
 *
 * @author makejava
 * @since 2022-12-26 15:42:24
 */
@Data
public class MemberClockInAddRequest {

    @ApiModelProperty(value = "打卡目标地点编号",example = "ss【宿舍】 jxl【教学楼】")
    @NotBlank
    private String siteCode;

    @ApiModelProperty(value = "班级ID")
    @NotNull(message = "clazz NULL")
    private Long clazzId;

    @ApiModelProperty(value = "学员打卡经度")
    @NotBlank
    private String longitude;

    @ApiModelProperty(value = "学员打卡纬度")
    @NotBlank
    private String latitude;

    @ApiModelProperty(value = "备注")
    private String remark;

}

