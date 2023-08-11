package cn.qbs.wa.teach.basic.api;

import cn.qbs.wa.teach.basic.api.factory.RemoteUserFallbackFactory;
import cn.qbs.wa.teach.basic.api.pojo.DTO.app.ApplicationFullDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.app.ApplicationFullResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteAppService", name="teach-basic", path = "basic/app",fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteAppService {


    @PostMapping("full-list")
    @ApiOperation("获取所有应用列表")
    R<List<ApplicationFullResultDTO>> getFullList(@RequestBody ApplicationFullDTO applicationFullDTO) ;



}
