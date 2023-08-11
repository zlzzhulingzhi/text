package cn.qbs.wa.teach.common.live.pojo.vod;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/14 15:42
 */
@Data
public class VodMetaDataResponse {

    private Long Size;

    private String Container;

    private Long Bitrate;

    private Integer Height;

    private Integer Width;

    private Long Duration;

    private Integer Rotate;

    private Double VideoDuration;

    private Double AudioDuration;
}
