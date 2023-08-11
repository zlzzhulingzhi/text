package cn.qbs.wa.teach.common.live.pojo.vod;

import lombok.Data;

@Data
public class VodDownloadRequest {

    private String fileId;

    private final Long DEFINITION = 100040L;
}
