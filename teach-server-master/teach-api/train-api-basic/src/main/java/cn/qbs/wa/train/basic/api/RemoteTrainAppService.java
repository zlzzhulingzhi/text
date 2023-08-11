package cn.qbs.wa.train.basic.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.api.factory.RemoteTrainAppFallbackFactory;
import cn.qbs.wa.train.basic.api.pojo.DTO.app.ApplicationFullDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.app.ApplicationFullResultDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteTrainAppService", name="train-basic", path = "/train/basic/app",fallbackFactory = RemoteTrainAppFallbackFactory.class)
public interface RemoteTrainAppService {

    @PostMapping("full-list")
    @ApiOperation("获取所有应用列表")
    R<List<ApplicationFullResultDTO>> getFullList(@RequestBody ApplicationFullDTO applicationFullDTO) ;

}
