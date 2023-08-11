package cn.qbs.wa.teach.organization.api.pojo.DTO.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 职工信息
 * @author Administrator
 */
@Data
public class EmployeeDTO {

    private Long id;

    private Long employeeId;

    private String realName;

    private String phone;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private Long updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    private Long orgId;

    private Long userId;

    private Integer sex;

    private Integer enabled;

    public Long getEmployeeId() {
        return Optional.ofNullable(id).orElse(employeeId);
    }
}
