package cn.qbs.wa.teach.organization.api.pojo.DTO.student;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SocialBindingDTO extends StudentDTO {
    private String unionId;
}
