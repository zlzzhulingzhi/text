package cn.qbs.wa.teach.course.standard.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SignUpDTO {

    @NotNull(message = "课程ID不为空")
    @ApiModelProperty(value = "【课程ID】")
    private Long courseId;

    @NotNull(message = "会员ID不为空")
    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【真实姓名】")
    private String realName;

    @ApiModelProperty(value = "【昵称】")
    private String nickName;

    @ApiModelProperty(value = "【头像地址】")
    private String headImgUrl;
}
