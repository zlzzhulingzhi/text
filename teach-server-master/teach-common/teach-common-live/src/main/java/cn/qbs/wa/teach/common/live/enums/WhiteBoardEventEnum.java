package cn.qbs.wa.teach.common.live.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WhiteBoardEventEnum {


    START("WhiteboardPushStarted", "开始"),

    STOP("WhiteboardPushStopped", "结束");



    private final String code;

    private final String codeName;
}
