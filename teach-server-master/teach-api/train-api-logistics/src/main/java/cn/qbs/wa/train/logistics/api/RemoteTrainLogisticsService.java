package cn.qbs.wa.train.logistics.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.factory.RemoteTrainLogisticsFallbackFactory;
import cn.qbs.wa.train.logistics.api.pojo.DTO.OrganizationAddRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.OrganizationUpdateRequestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteTrainLogisticsService", name = "train-logistics", path = "/logistics/inner/organization", fallbackFactory = RemoteTrainLogisticsFallbackFactory.class)
public interface RemoteTrainLogisticsService {

    @PostMapping("init")
    @ApiOperation("初始化组织机构")
    R<Boolean> init(@RequestBody OrganizationAddRequestDTO params);

    @PostMapping("/admin/update")
    @ApiOperation("更新组织机构")
    R<Boolean> adminUpdate(@RequestBody OrganizationUpdateRequestDTO params);

    @PostMapping("update")
    @ApiOperation("更新组织机构")
    R<Boolean> update(@RequestBody OrganizationUpdateRequestDTO params);

}
