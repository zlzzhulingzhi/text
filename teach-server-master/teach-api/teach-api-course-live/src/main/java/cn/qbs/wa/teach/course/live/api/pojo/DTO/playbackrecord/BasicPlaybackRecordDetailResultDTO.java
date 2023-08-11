package cn.qbs.wa.teach.course.live.api.pojo.DTO.playbackrecord;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 基础直播回放表(BasicPlaybackRecord)基础直播回放表详情
 *
 * @author makejava
 * @since 2021-12-20 16:46:46
 */
@Data
public class BasicPlaybackRecordDetailResultDTO {


    @ApiModelProperty(value = "回放地址")
    List<String> playbackUrls;

}

