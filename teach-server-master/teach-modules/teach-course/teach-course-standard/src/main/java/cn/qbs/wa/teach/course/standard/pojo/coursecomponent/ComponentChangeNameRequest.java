package cn.qbs.wa.teach.course.standard.pojo.coursecomponent;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 更新名称参数
 *
 * @Author zcm
 * @Date 2022-06-10 10:45
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ComponentChangeNameRequest extends IdRequest {

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;

}

