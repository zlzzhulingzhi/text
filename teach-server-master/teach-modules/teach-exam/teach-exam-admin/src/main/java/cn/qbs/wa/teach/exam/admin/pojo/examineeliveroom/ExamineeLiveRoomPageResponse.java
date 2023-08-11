package cn.qbs.wa.teach.exam.admin.pojo.examineeliveroom;

import cn.qbs.wa.teach.exam.common.entity.ExamineeLiveRoom;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考生直播房间(ExamineeLiveRoom)分页查询考生直播房间响应
 *
 * @author makejava
 * @since 2022-01-04 11:44:40
 */
@Data
public class ExamineeLiveRoomPageResponse extends ExamineeLiveRoom {
    @ApiModelProperty(value = "用户名")
    private String realName;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "回放地址")
    private String playbackUrl;

    @ApiModelProperty(value = "")
    private Long userId;

    @ApiModelProperty(value = "是否有回放视频的标识, 0:无回放, 1:有回放")
    private Integer flag;


}

