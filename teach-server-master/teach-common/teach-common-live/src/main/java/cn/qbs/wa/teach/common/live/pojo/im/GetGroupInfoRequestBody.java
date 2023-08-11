package cn.qbs.wa.teach.common.live.pojo.im;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 获取群详细资料响应
 *
 * @Author zcm
 * @Date 2022-07-05 10:54
 * @Version 1.0
 */
@Data
public class GetGroupInfoRequestBody {

    @JSONField(name = "GroupIdList")
    private List<String> GroupIdList;

    @JSONField(name = "ResponseFilter")
    private ResponseFilter ResponseFilter;

    @Data
    public static class ResponseFilter {

        @JSONField(name = "GroupBaseInfoFilter")
        private List<String> GroupBaseInfoFilter;

        @JSONField(name = "MemberInfoFilter")
        private List<String> MemberInfoFilter;

    }

}
