package cn.qbs.wa.teach.common.live.pojo;

import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/24 10:55
 */
@Data
public class LiveRequest {

    //结束时间
    private Long endTime;

    private Long roomId;

    private String userId;
}
