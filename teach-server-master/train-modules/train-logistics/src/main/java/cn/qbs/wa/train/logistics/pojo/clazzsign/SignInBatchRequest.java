package cn.qbs.wa.train.logistics.pojo.clazzsign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignInBatchRequest extends SignInAddRequest {

    @NotNull
    @ApiModelProperty(value = "签到模式 1:选择部分学生 2:选择班级 ", example = "2")
    private Integer selectMode;

    @ApiModelProperty(value = "学生用户id数组(选部分学生使用)")
    private List<Long> memberIds;

    @NotNull(message = "请选择签到课次")
    @ApiModelProperty(value = "课节次数组")
    private List<Integer> lessonNums;

}
