package cn.qbs.wa.train.logistics.pojo.clazzsign;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignInPageRequest extends BasePageRequest {

    @NotNull(message = "请选择班级")
    @ApiModelProperty("班级ID")
    private Long clazzId;

    @ApiModelProperty(value = "机构ID", hidden = true)
    private Long orgId;

    @ApiModelProperty(value = "签到情况-开始节数", example = "1")
    private Integer lessonStart;

    @ApiModelProperty(value = "签到情况-结束节数", example = "10")
    private Integer lessonEnd;

    @ApiModelProperty(value = "1-在读 2-停课", example = "1")
    private Integer studentStatus;

}
