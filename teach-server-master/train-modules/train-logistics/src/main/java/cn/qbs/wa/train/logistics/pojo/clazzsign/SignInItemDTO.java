package cn.qbs.wa.train.logistics.pojo.clazzsign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignInItemDTO {

    @ApiModelProperty(value = "主键标识", example = "1")
    private Long signInId;

    @ApiModelProperty(value = "签到日期", example = "2022-05-01")
    private LocalDate signInDate;

    @ApiModelProperty(value = "是否能编辑", example = "1")
    private Integer editable;

    @ApiModelProperty(value = "签到第几节", example = "1")
    private Integer lessonNum;

    @ApiModelProperty(value = "签到1节要消耗的课时数", example = "1")
    private Integer lessonHour;

    @ApiModelProperty(value = "签到状态 ", example = "【签到状态 0未签到 1已签到】")
    private Integer signInStatus;

    @ApiModelProperty(value = "备注 ")
    private String remark;

}
