package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteOrgBackRoleFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * @author Wx
 * 机构后台优惠券所使用的远程接口
 */
@FeignClient(contextId = "remoteOrgBackRoleService", name = "teach-org", path = "org/organizationRole", fallbackFactory = RemoteOrgBackRoleFallbackFactory.class)
public interface RemoteOrgBackRoleService {


    @PostMapping("/getOrgRole")
    @ApiOperation("获取机构角色列表")
    R<List<OrgDeptOrRoleResponseDTO>> getOrgRole(@RequestBody Serializable id);

}

