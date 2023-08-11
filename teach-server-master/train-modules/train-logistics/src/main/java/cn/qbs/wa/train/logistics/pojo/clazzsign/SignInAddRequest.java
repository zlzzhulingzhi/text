package cn.qbs.wa.train.logistics.pojo.clazzsign;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class SignInAddRequest {

    @NotNull(message = "班级ID不为空")
    @ApiModelProperty("班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "签到课时数目", example = "1")
    private Integer lessonHour;

    @NotNull(message = "请选择签到日期")
    @ApiModelProperty(value = "签到日期", example = "2022-10-09")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate signInDate;

    @NotNull(message = "签到状态不能为空")
    @ApiModelProperty(value = "1-签到 2-请假 3-旷课", example = "1")
    private Integer signInStatus;

    @ApiModelProperty(value = "备注")
    private String remark;
}
