package cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LecturerDTO {
    private Long id;

    private String realName;

    private String lecturerName;

    private String phone;

    private Long orgId;

    private Long userId;

    private Long employeeId;

    private String intro;

    private String headImgUrl;

    private Integer enabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    private Long updateBy;

    @ApiModelProperty(value = "头衔")
    private String title;
}
