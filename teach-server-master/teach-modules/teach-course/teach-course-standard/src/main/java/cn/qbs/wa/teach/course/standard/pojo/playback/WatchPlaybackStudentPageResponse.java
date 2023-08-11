package cn.qbs.wa.teach.course.standard.pojo.playback;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分页查询观看回放学员响应结果
 *
 * @Author zcm
 * @Date 2022-06-24 9:48
 * @Version 1.0
 */
@Data
public class WatchPlaybackStudentPageResponse {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "学员姓名")
    private String studentName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "首次进入直播间时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime firstJoinTime;

    @ApiModelProperty(value = "进入直播间次数")
    private Integer joinTimes;

    @ApiModelProperty("累计观看时长")
    private String cumulativeWatchTime;

}

