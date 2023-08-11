package cn.qbs.wa.teach.course.standard.pojo.playback;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 回放统计
 *
 * @Author zcm
 * @Date 2022-06-24 9:10
 * @Version 1.0
 */
@Data
public class PlaybackStatistic {

    @ApiModelProperty("累计观看人数")
    private Integer cumulativeUsers;

    @ApiModelProperty("累计观看人次")
    private Integer cumulativeTimes;

    @ApiModelProperty("人均观看时长")
    private Integer avgWatchTime;

}
