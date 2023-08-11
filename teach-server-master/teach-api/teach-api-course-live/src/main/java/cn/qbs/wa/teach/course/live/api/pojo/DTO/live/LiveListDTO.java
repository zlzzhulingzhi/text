package cn.qbs.wa.teach.course.live.api.pojo.DTO.live;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/30 10:15
 */
@Data
public class LiveListDTO {

    @ApiModelProperty("机构id")
    Long orgId;

    @ApiModelProperty("直播名称")
    String liveName;

    @ApiModelProperty("职工id数组")
    List<Long> idList;

    @ApiModelProperty(value = "上架状态 0 下架 1上架")
    private Integer shelfStatus;
}
