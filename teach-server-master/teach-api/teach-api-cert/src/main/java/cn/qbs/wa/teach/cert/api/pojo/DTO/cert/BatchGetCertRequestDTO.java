package cn.qbs.wa.teach.cert.api.pojo.DTO.cert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: NQY
 * @Date: 2022/5/18 9:02
 * @Description:
 */
@Data
public class BatchGetCertRequestDTO {
    @ApiModelProperty(value = "证书id")
    private List<Long> certIds;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "来源id")
    private Long sourceId;

    @ApiModelProperty(value = "是否作废 , 0: 作废, 1:正常")
    private Integer enabled;
}
