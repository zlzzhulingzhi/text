package cn.qbs.wa.union.admin.pojo.uniapplicationuser;

import cn.qbs.wa.union.admin.entity.UniApplication;
import lombok.Data;

/**
 * 【系统应用-用户】(UniApplicationUser)【系统应用-用户】详情
 *
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
@Data
public class UniApplicationUserDetailResponse extends UniApplication {

    private Long applicationUserId;

    private Boolean accessed;

    public Boolean getAccessed() {
        return applicationUserId!=null;
    }
}

