package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteOrgBackDeptService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.DeptListRequestDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.TreeDeptTotalResponseDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.io.Serializable;
import java.util.List;

/**
 * @author WX
 */
public class RemoteOrgBackDeptFallbackFactory implements FallbackFactory<RemoteOrgBackDeptService> {

    @Override
    public RemoteOrgBackDeptService create(Throwable cause) {
        return new RemoteOrgBackDeptService() {
            @Override
            public R<List<OrgDeptOrRoleResponseDTO>> getOrgDept(Serializable id) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<TreeDeptTotalResponseDTO>  treeList(DeptListRequestDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R detailList(IdListRequest request) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
