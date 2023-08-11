package cn.qbs.wa.train.logistics.pojo.clazzsign;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignInRecordResponse extends SignInItemDTO {

    @ApiModelProperty(value = "学生用户id")
    private Long memberId;

    @ApiModelProperty(value = "学生名字")
    private String studentName;

    @EncodeContent(decode = false, desensitized = true)
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "班级名字")
    private String clazzName;
}
