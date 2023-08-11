package cn.qbs.wa.train.logistics.pojo.membervisit;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 学员访问管理(MemberVisit)创建学员访问管理参数
 *
 * @author makejava
 * @since 2022-12-28 16:24:21
 */
@Data
public class MemberVisitAddRequest {

    @ApiModelProperty(value = "访客名称")
    private String visitorName;

    @ApiModelProperty(value = "学员用户ID")
    private Long memberId;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "车牌号")
    private String carNumber;

    @ApiModelProperty(value = "访问人物")
    private String visitPeople;

    @ApiModelProperty(value = "访问部门")
    private String visitDepartment;

    @ApiModelProperty(value = "访问原因")
    private String visitReason;

    @ApiModelProperty(value = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime visitTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "访问机构ID")
    private Long visitOrgId;

}

