package cn.qbs.wa.teach.out.union.admin.api;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.out.union.admin.api.DTO.InnerEmployeeUserAddDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.InnerSystemUserUpdateDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.SystemEmployeeUserAddResultDTO;
import cn.qbs.wa.teach.out.union.admin.api.factory.RemoteSystemEmployeeUserFallbackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "remoteUnionSystemEmployeeUserService", name = "union-admin", path = "u-admin/systemEmployeeUser", fallbackFactory = RemoteSystemEmployeeUserFallbackFactory.class)
public interface RemoteUnionSystemEmployeeUserService {

    @PostMapping("inner-add")
    @ApiOperation("内部新增")
    R<SystemEmployeeUserAddResultDTO> innerAdd(@RequestBody InnerEmployeeUserAddDTO params);


    @PostMapping("inner-update")
    @ApiOperation("内部更新")
    R innerUpdate(@RequestBody InnerSystemUserUpdateDTO params);
}
