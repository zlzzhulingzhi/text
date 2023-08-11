package cn.qbs.wa.train.logistics.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.factory.RemoteTrainLogisticsEmployeeFallbackFactory;
import cn.qbs.wa.train.logistics.api.pojo.DTO.LoginInfoDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.EmployeeAddRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.EmployeeDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.EmployeeUpdateRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.UpdateBindUserDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteTrainLogisticsEmployeeService", name = "train-logistics", path = "/logistics/inner/employee", fallbackFactory = RemoteTrainLogisticsEmployeeFallbackFactory.class)
public interface RemoteTrainLogisticsEmployeeService {

    @PostMapping("add")
    @ApiOperation("新增职工")
    R<EmployeeDTO> add(@RequestBody EmployeeAddRequestDTO params);

    @PostMapping("admin/update")
    @ApiOperation("管理员更新职工")
     R adminUpdate(@RequestBody @Validated EmployeeUpdateRequestDTO params);

    @PostMapping("/login-info")
    R<LoginInfoDTO> getLoginInfo(@RequestBody IdRequest request);

    @PostMapping("/updateBindUser")
    R<Boolean> updateBindUser(@RequestBody UpdateBindUserDTO requestDTO);
}
