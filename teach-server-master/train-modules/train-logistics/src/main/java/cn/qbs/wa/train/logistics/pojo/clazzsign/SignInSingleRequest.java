package cn.qbs.wa.train.logistics.pojo.clazzsign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignInSingleRequest extends SignInAddRequest {

    @NotNull(message = "请选择签到课次")
    @ApiModelProperty(value = "签到课次", example = "1")
    private Integer lessonNum;

    @NotNull(message = "学生用户ID不为空")
    @ApiModelProperty(value = "学生用户id")
    private Long memberId;

    @ApiModelProperty(value = "保留字段")
    private Long signInId;

}
