package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteEmployeeDeptFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.EmployeeDeptListSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.EmployeeDeptTreeListDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteEmployeeDeptService", name = "teach-org", path = "org/employeeDept", fallbackFactory = RemoteEmployeeDeptFallbackFactory.class)
public interface RemoteEmployeeDeptService {


    @PostMapping("/list/tree")
    @ApiOperation("职工部门表树形")
    R<List<EmployeeDeptTreeListDTO>> listTree(@RequestBody EmployeeDeptListSearchDTO params);

    @PostMapping("/list")
    @ApiOperation("职工部门表")
    R<List<EmployeeDeptTreeListDTO>> list(@RequestBody EmployeeDeptListSearchDTO params);

}

