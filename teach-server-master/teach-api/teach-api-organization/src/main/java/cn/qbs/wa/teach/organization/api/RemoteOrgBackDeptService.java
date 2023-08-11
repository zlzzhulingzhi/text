package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteOrgBackDeptFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.DeptDetailResponseDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.DeptListRequestDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.TreeDeptTotalResponseDTO;
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
@FeignClient(contextId = "remoteOrgBackDeptService", name = "teach-org", path = "org/dept", fallbackFactory = RemoteOrgBackDeptFallbackFactory.class)
public interface RemoteOrgBackDeptService {


    @PostMapping("/getOrgDept")
    @ApiOperation("获取机构部门列表")
    R<List<OrgDeptOrRoleResponseDTO>> getOrgDept(@RequestBody Serializable id);

    @PostMapping("tree-list")
    @ApiOperation("机构管理员部门树形列表显示")
    R<TreeDeptTotalResponseDTO> treeList(@RequestBody DeptListRequestDTO params);

    @PostMapping("detailList")
    @ApiOperation("部门详情集合")
    R<List<DeptDetailResponseDTO>> detailList(@RequestBody IdListRequest request);
}

