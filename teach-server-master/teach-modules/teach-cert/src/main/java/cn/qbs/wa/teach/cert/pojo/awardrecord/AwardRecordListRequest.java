package cn.qbs.wa.teach.cert.pojo.awardrecord;


import cn.qbs.wa.teach.domain.BasePageRequest;
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
public class AwardRecordListRequest {

    @ApiModelProperty(value = "机构Id",hidden = true)
    private Long orgId;

    @ApiModelProperty(value = "证书Id")
    private Long certId;

    @ApiModelProperty(value = "筛选查询列表")
    private List<AwardRecordSearchList> searchList;



}

