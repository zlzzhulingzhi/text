package cn.qbs.wa.teach.common.live.pojo.vod;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/14 15:41
 */
@Data
public class VodClipResponse {

    private String Url;

    private String FileId;

    private String RequestId;

    private VodMetaDataResponse MetaData;
}
