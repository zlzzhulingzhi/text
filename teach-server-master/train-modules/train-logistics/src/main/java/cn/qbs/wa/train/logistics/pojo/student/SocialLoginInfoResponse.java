package cn.qbs.wa.train.logistics.pojo.student;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SocialLoginInfoResponse extends LoginInfoResponse {

    private String uid;
}
