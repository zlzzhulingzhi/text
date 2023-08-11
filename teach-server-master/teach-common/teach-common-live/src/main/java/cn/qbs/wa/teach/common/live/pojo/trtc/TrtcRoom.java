package cn.qbs.wa.teach.common.live.pojo.trtc;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/3 15:01
 */
@Data
public class TrtcRoom {

    private String CommId;

    private String RoomString;

    private long CreateTime;

    private long DestroyTime;

    private Boolean IsFinished;

    private String UserId;
}
