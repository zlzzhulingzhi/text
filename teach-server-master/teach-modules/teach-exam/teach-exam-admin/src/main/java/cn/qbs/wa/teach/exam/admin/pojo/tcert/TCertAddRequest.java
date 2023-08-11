package cn.qbs.wa.teach.exam.admin.pojo.tcert;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 任务证书表(TCert)创建任务证书表参数
 *
 * @author makejava
 * @since 2022-05-16 13:48:02
 */
@Data
public class TCertAddRequest {
    
    @ApiModelProperty(value = "机构id")
    private Long orgId;
    
    @ApiModelProperty(value = "考试id")
    private Long examId;
    
    @ApiModelProperty(value = "删除")
    private Integer deleted;

    @ApiModelProperty(value = "证书id")
    @NotNull(message = "证书id不能为空")
    private Long certId;

    @ApiModelProperty(value = "发放条件,0:通过考试,1:完成考试")
    private Integer ruleValue;
}

