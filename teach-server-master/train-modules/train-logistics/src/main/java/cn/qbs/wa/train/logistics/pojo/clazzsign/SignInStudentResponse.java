package cn.qbs.wa.train.logistics.pojo.clazzsign;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yjx
 */
@Data
public class SignInStudentResponse {

    @ApiModelProperty(value = "学生用户id")
    private Long memberId;

    @ApiModelProperty(value = "学生名字")
    private String studentName;

    @EncodeContent(decode = false, desensitized = true)
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "正常上课节数")
    private Integer normalCount;

    @ApiModelProperty(value = "签到情况")
    private List<SignInItemDTO> signInList;
}
