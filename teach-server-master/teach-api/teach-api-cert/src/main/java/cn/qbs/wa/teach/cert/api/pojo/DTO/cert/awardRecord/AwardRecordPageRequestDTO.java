package cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord;



import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 颁发记录(AwardRecord)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:18
 */
@Data
public class AwardRecordPageRequestDTO extends BasePageSearchComDTO {

    @ApiModelProperty(value = "机构Id",hidden = true)
    private Long orgId;

    @ApiModelProperty(value = "证书Id")
    private Long certId;

    @ApiModelProperty(value = "来源id")
    private Long sourceId;

    @ApiModelProperty(value = "筛选查询列表")
    private List<AwardRecordSearchListDTO> searchList;
}

