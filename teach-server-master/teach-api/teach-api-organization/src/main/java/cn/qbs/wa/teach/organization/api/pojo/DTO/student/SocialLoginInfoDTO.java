package cn.qbs.wa.teach.organization.api.pojo.DTO.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yjx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginInfoDTO {

    private Long orgId;

    private String unionId;

}
