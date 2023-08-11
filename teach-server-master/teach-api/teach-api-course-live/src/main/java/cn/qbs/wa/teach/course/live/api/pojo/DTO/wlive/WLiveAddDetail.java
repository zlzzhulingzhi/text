package cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/3/3 17:12
 */
@Data
public class WLiveAddDetail {


    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "业务类型 1 直播课程")
    private Integer businessType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "业务名称")
    private String businessName;
}
