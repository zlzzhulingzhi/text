package cn.qbs.wa.teach.cert.pojo.cert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/22 9:59
 */
@Data
public class CertExamResponse {

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "考试用户列表")
    private List<CertUserResponse> examUserList;
}
