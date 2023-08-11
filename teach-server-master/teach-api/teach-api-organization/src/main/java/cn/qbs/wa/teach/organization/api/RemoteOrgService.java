package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteOrgFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationInnerDetailResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "remoteOrgService", name = "teach-org", path = "org/inner/organization", fallbackFactory = RemoteOrgFallbackFactory.class)
public interface RemoteOrgService {

    @PostMapping("detail")
    R<OrganizationInnerDetailResponseDTO> detail(@RequestBody IdRequest request);

}
