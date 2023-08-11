package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteOrgBackRoleService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.io.Serializable;
import java.util.List;

/**
 * @author WX
 */
public class RemoteOrgBackRoleFallbackFactory implements FallbackFactory<RemoteOrgBackRoleService> {

    @Override
    public RemoteOrgBackRoleService create(Throwable cause) {
        return new RemoteOrgBackRoleService() {
            @Override
            public R<List<OrgDeptOrRoleResponseDTO>> getOrgRole(Serializable id) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
