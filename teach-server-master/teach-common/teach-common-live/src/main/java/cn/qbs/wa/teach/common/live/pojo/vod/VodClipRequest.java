package cn.qbs.wa.teach.common.live.pojo.vod;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/14 13:54
 */
@Data
public class VodClipRequest {

    private String fileId;

    private String url;

    private Float startTimeOffset;

    private Float endTimeOffset;
}
