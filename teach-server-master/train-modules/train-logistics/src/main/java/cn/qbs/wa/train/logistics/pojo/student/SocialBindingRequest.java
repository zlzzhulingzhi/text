package cn.qbs.wa.train.logistics.pojo.student;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author yjx
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SocialBindingRequest extends StudentAddRequest {

    @NotBlank(message = "uid不能为空")
    private String unionId;
}
