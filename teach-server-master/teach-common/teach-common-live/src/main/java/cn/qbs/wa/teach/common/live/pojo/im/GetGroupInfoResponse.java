package cn.qbs.wa.teach.common.live.pojo.im;

import lombok.Data;

import java.util.List;

/**
 * 获取群详细资料请求体
 *
 * @Author zcm
 * @Date 2022-07-05 10:54
 * @Version 1.0
 */
@Data
public class GetGroupInfoResponse {

    private String ActionStatus;
    private Integer ErrorCode;
    private String ErrorInfo;
    private List<GroupInfo> GroupInfo;

    @Data
    public static class GroupInfo {

        private Integer Appid;
        private String ApplyJoinOption;
        private Integer CreateTime;
        private Integer ErrorCode;
        private String FaceUrl;
        private String GroupId;
        private Integer InfoSeq;
        private String Introduction;
        private Integer LastInfoTime;
        private Integer LastMsgTime;
        private Integer MaxMemberNum;
        private List<MemberList> MemberList;
        private Integer MemberNum;
        private String Name;
        private Integer NextMsgSeq;
        private String Notification;
        private Integer OnlineMemberNum;
        private String Owner_Account;
        private String ShutUpAllMember;
        private String Type;

    }

    @Data
    public static class MemberList {

        private Integer JoinTime;
        private Integer LastSendMsgTime;
        private String Member_Account;
        private String MsgFlag;
        private Integer MsgSeq;
        private String NameCard;
        private String Role;
        private Integer ShutUpUntil;

    }

}
