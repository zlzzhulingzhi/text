package cn.qbs.wa.teach.out.union.admin.api;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.out.union.admin.api.DTO.InnerSystemUserAddDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.InnerSystemUserAddResultDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.InnerSystemUserUpdateDTO;
import cn.qbs.wa.teach.out.union.admin.api.factory.RemoteSystemUserFallbackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "remoteUnionSystemUserService", name = "union-admin", path = "u-admin/systemUser", fallbackFactory = RemoteSystemUserFallbackFactory.class)
public interface RemoteUnionSystemUserService {

    @PostMapping("inner-add")
    @ApiOperation("内部新增")
    R<InnerSystemUserAddResultDTO> innerAdd(@RequestBody InnerSystemUserAddDTO params);

    @PostMapping("inner-update")
    @ApiOperation("内部更新")
    R innerUpdate(@RequestBody InnerSystemUserUpdateDTO params);
}
