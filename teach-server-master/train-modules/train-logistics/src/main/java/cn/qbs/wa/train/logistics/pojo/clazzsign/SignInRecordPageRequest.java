package cn.qbs.wa.train.logistics.pojo.clazzsign;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignInRecordPageRequest extends SignInPageRequest {

    @ApiModelProperty("学员ID")
    private Long memberId;

    @ApiModelProperty(value = "签到日期-开始")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate startDate;

    @ApiModelProperty(value = "签到日期-结束")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate endDate;

}
