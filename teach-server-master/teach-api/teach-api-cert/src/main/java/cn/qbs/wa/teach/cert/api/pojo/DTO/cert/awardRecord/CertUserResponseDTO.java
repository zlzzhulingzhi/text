package cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CertUserResponseDTO {

    @ApiModelProperty(value = "id占位")
    private Long id;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "身份证号码")
    private String idNum;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "考试分数")
    private BigDecimal score;

    @ApiModelProperty(value = "考试名称")
    private String examName;


    @ApiModelProperty(value = "禁用0 启用1")
    private Integer enabled;

    @ApiModelProperty(value = "0未颁发 1已颁发")
    private Integer awarded=0;

    @ApiModelProperty(value = "考生名称", hidden = true)
    @JsonIgnore
    private String examineeName;

    @ApiModelProperty(value = "学员ID")
    private Long studentId;

    public String getUserName() {
        if (StrUtil.isBlank(userName)) {
            return examineeName;
        }
        return userName;
    }

    public Long getstudentId() {
        if (studentId == null) {
            return id;
        }
        return studentId;
    }
}
