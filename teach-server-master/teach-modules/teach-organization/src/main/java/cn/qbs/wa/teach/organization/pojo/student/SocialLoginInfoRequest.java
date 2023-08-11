package cn.qbs.wa.teach.organization.pojo.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yjx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginInfoRequest {

    private Long orgId;

    private String unionId;

}
