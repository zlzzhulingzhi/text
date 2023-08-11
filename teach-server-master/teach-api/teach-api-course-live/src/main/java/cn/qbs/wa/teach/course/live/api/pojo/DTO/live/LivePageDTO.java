package cn.qbs.wa.teach.course.live.api.pojo.DTO.live;

import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/29 15:30
 */
@Data
public class LivePageDTO extends BasePageSearchComDTO {


    @ApiModelProperty(value = "直播名称")
    private String liveName;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "是否展示有效的 0 否 1是")
    private Integer effected=1;

    @ApiModelProperty(value = "id数组")
    private List<Long> idList;

    @ApiModelProperty(value = "机构id")
    private Long orgId;
}
