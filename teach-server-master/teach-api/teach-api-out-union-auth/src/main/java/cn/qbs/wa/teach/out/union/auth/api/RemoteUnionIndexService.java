package cn.qbs.wa.teach.out.union.auth.api;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.out.union.auth.api.DTO.TokenInfoDTO;
import cn.qbs.wa.teach.out.union.auth.api.DTO.TokenInfoResultDTO;
import cn.qbs.wa.teach.out.union.auth.api.factory.RemoteUnionIndexFallbackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "remoteUnionAuthIndexService", name = "union-auth", path = "u-auth/index", fallbackFactory = RemoteUnionIndexFallbackFactory.class)
public interface RemoteUnionIndexService {


    @PostMapping("token-info")
    @ApiOperation("token校验")
    R<TokenInfoResultDTO> checkToken(@RequestBody TokenInfoDTO request);
}
