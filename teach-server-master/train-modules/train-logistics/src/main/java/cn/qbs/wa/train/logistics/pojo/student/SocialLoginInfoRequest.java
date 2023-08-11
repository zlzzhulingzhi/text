package cn.qbs.wa.train.logistics.pojo.student;

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
