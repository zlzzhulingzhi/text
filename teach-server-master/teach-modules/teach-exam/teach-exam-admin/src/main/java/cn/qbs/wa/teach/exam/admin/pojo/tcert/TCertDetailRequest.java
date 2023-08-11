package cn.qbs.wa.teach.exam.admin.pojo.tcert;

import cn.qbs.wa.teach.exam.common.entity.TCert;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务证书表(TCert)任务证书表详情
 *
 * @author makejava
 * @since 2022-05-16 13:48:02
 */
@Data
public class TCertDetailRequest extends TCert {
    @ApiModelProperty(value = "证书id")
    private Long certId;

}

