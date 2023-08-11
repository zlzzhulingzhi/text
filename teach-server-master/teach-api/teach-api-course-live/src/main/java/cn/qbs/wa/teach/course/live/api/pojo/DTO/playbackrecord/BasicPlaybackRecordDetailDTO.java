package cn.qbs.wa.teach.course.live.api.pojo.DTO.playbackrecord;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础直播回放表(BasicPlaybackRecord)基础直播回放表详情
 *
 * @author makejava
 * @since 2021-12-20 16:46:46
 */
@Data
public class BasicPlaybackRecordDetailDTO {


    /**
     * 　　两个参数选一即可查询出来
     * 　
     *
     */

    @ApiModelProperty(value = "流名称")
    private String streamName;

    @ApiModelProperty(value = "基础直播id")
    private Long basicLiveId;

}

