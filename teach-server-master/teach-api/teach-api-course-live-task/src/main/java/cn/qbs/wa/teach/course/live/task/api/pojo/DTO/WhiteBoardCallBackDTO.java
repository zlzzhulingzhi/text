package cn.qbs.wa.teach.course.live.task.api.pojo.DTO;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/23 17:40
 */
@Data
public class WhiteBoardCallBackDTO {

    private WhiteBoardCallBackDataDTO EventData;

    private String EventType;

    private Long ExpireTime;

    private Long SdkAppId;

    private String Sign;

    private Long Timestamp;
}
