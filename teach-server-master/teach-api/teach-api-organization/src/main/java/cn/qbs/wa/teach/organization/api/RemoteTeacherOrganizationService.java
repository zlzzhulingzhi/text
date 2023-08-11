package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteOrganizationFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteTeacherOrganizationService", name = "teach-org", path = "org", fallbackFactory = RemoteOrganizationFallbackFactory.class)
public interface RemoteTeacherOrganizationService {

    @PostMapping("/organization/list")
    @ApiOperation("列表显示")
    R<List<OrganizationListResultDTO>> list();

    @PostMapping("/organization/info")
    @ApiOperation("机构信息")
    R<OrganizationListResultDTO> info(@RequestBody IdRequest request);

    @PostMapping("inner/organization/init")
    @ApiOperation("初始化组织机构")
    R<Long> init(@RequestBody TeacherOrganizationAddRequestDTO params);

    @PostMapping("inner/organization/update")
    @ApiOperation("更新组织机构")
    R<Boolean> update(@RequestBody TeacherOrganizationInnerUpdateRequestDTO params);

    @PostMapping("inner/organization/list")
    @ApiOperation("更新组织机构")
    R<List<OrganizationListInnerResultDTO>> list(@RequestBody OrganizationListInnerDTO params);

}
