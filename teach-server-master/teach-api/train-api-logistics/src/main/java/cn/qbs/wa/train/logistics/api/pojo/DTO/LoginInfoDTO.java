package cn.qbs.wa.train.logistics.api.pojo.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class LoginInfoDTO {

    private Long orgId;

    private Long employeeId;

    private Set<String> permissions;

}
