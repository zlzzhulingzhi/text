package cn.qbs.wa.teach.common.live.pojo.board;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/23 17:40
 */
@Data
public class WhiteBoardCallBackRequest {

    private WhiteBoardCallBackData EventData;

    private String EventType;

    private Long ExpireTime;

    private Long SdkAppId;

    private String Sign;

    private Long Timestamp;
}
