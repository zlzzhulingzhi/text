package cn.qbs.wa.teach.organization.api.pojo.DTO.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author yjx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoDTO {

    private Long orgId;

    private String account;

    private Long employeeId;

    private Set<String> permissions;

    public LoginInfoDTO(Long orgId, String account) {
        this.orgId = orgId;
        this.account = account;
    }
}
