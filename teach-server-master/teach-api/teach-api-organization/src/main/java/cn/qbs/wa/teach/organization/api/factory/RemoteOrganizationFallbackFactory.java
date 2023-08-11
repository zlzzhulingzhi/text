package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteTeacherOrganizationService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.*;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @author Administrator
 */
public class RemoteOrganizationFallbackFactory implements FallbackFactory<RemoteTeacherOrganizationService> {


    @Override
    public RemoteTeacherOrganizationService create(Throwable cause) {
        return new RemoteTeacherOrganizationService() {

            @Override
            public R<OrganizationListResultDTO> info(IdRequest request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<Long> init(TeacherOrganizationAddRequestDTO params) {
                return null;
            }

            @Override
            public R<Boolean> update(TeacherOrganizationInnerUpdateRequestDTO params) {
                return null;
            }

            @Override
            public R<List<OrganizationListResultDTO>> list() {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<OrganizationListInnerResultDTO>> list(OrganizationListInnerDTO params) {
                return null;
            }
        };
    }
}
