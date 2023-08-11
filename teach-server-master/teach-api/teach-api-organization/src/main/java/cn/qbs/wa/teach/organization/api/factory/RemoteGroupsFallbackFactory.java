package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteGroupsService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.groups.GroupsDetailResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @author WX
 */
public class RemoteGroupsFallbackFactory implements FallbackFactory<RemoteGroupsService> {

    @Override
    public RemoteGroupsService create(Throwable cause) {
        return new RemoteGroupsService() {
            @Override
            public R<List<GroupsDetailResponseDTO>> list() {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<GroupsDetailResponseDTO>> detailList(IdListRequest request) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
