package cn.qbs.wa.teach.course.standard.pojo.dto;

import lombok.Data;

/**
 * @author yjx
 */
@Data
public class TemporaryCredential {
    private String accessKey;
    private String secretKey;
    private String securityToken;
}
