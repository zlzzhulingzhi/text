package cn.qbs.wa.teach.common.live.pojo.trtc;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/3 14:59
 */
@Data
public class TrtcRoomInformationPageResponse {

    private Integer Total;

    private List<TrtcRoom> RoomList;

    private String RequestId;


}
