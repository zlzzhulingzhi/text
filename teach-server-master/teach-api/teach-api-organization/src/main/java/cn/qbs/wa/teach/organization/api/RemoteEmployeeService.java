package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteEmployeeFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.*;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.UpdateBindUserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteEmployeeService", name = "teach-org", path = "org/employee", fallbackFactory = RemoteEmployeeFallbackFactory.class)
public interface RemoteEmployeeService {

    @PostMapping("inner/add")
    @ApiOperation("新增职工")
    R<EmployeeDTO> add(@RequestBody OrganizationEmployeeAddRequestDTO params);

    @PostMapping("admin/list")
    @ApiOperation("管理员职工列表")
    R<List<EmployeeListResultDTO>> adminList(@RequestBody EmployeeListSearchDTO params);


    @PostMapping("list")
    @ApiOperation("职工列表")
    R<List<EmployeeListResultDTO>> list(@RequestBody EmployeeListSearchDTO params);

    @PostMapping("list-full")
    @ApiOperation("职工列表包含角色部门信息")
     R<List<EmployeeListFullResultDTO>> listFull(@RequestBody EmployeeListSearchDTO params) ;

    @PostMapping("/register")
    @ApiOperation("职工注册")
     R<EmployeeDTO> register(@RequestBody EmployeeRegisterDTO params) ;

    @PostMapping("page")
    @ApiOperation("机构管理员分页查询职工")
    R<PageResultComDTO<EmployeePageResultDTO>> page(@RequestBody EmployeePageSearchDTO params) ;

    @PostMapping("/inner/login-info")
    R<EmployeeDTO> getLoginInfo(@RequestBody IdRequest request);

    @PostMapping("inner/updateBindUser")
    R<Boolean> updateBindUser(@RequestBody UpdateBindUserDTO requestDTO);
}
