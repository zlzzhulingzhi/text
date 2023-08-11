package cn.qbs.wa.teach.common.live.pojo.board;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/24 9:42
 */
@Data
public class WhiteBoardCallBackData {


    private String PushUserId;

    private Integer RoomId;

    private String TaskId;

    private String GroupId;

    private String FinishReason;

    private Long PushStartTime;

    private Long PushStopTime;

    private Long IMSyncTime;

    private Integer ExceptionCnt;

}
