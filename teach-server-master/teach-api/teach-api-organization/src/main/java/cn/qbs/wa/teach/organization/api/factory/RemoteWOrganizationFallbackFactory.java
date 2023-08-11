package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteWOrganizationService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WOrgUpdateDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.worganization.WOrgAddDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.worganization.WOrgPageResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.worganization.WOrganizationPageSearchDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author Administrator
 */
public class RemoteWOrganizationFallbackFactory implements FallbackFactory<RemoteWOrganizationService> {


    @Override
    public RemoteWOrganizationService create(Throwable cause) {
        return new RemoteWOrganizationService() {
            @Override
            public R<PageResultComDTO<WOrgPageResultDTO>> page(WOrganizationPageSearchDTO params) {
                return null;
            }

            @Override
            public R add(WOrgAddDTO params) {
                return null;
            }

            @Override
            public R delete(IdListRequest request) {
                return null;
            }

            @Override
            public R<Boolean> update(WOrgUpdateDTO params) {
                return null;
            }
        };
    }
}
