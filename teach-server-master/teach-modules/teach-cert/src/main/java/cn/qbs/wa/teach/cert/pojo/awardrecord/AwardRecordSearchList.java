package cn.qbs.wa.teach.cert.pojo.awardrecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/2/16 16:40
 */
@Data
public class AwardRecordSearchList {

    @ApiModelProperty(value = "筛选code [userName 姓名,idNum 身份证号码,certNumRule 证书编号,phone,手机号码 ]")
    private String code;

    @ApiModelProperty(value = "筛选值")
    private String keyValue;
}
