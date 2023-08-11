package cn.qbs.wa.teach.organization.api.factory;


import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteOrgService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationInnerDetailResponseDTO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author yjx
 */
@Component
public class RemoteOrgFallbackFactory implements FallbackFactory<RemoteOrgService> {
    @Override
    public RemoteOrgService create(Throwable cause) {
        return new RemoteOrgService() {

            @Override
            public R<OrganizationInnerDetailResponseDTO> detail(IdRequest request) {
                return null;
            }
        };
    }
}
