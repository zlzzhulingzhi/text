package cn.qbs.wa.teach.course.live.api.pojo.DTO.liveshow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/31 17:34
 */
@Data
public class LiveShowUserSigDTO {

    @ApiModelProperty(value ="服务商code: txzb腾讯直播,txkzb腾讯快直播")
    private String providerTypeCode="txkzb";

    @ApiModelProperty(value = "1课程直播房间 100 监考直播房间")
    @NotNull(message = "直播房间类型不能为空")
    private Integer liveRoomType;

}
