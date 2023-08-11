package cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive;

import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/3/1 14:01
 */
@Data
public class WLivePageSearchDTO extends BasePageSearchComDTO {


    @ApiModelProperty(value = "机构id")
    private Long orgId;


    @ApiModelProperty(value = "id数组")
    private List<Long> idList;

    @ApiModelProperty(value = "是否展示有效的 0 否 1是")
    private Integer effected=1;

}
