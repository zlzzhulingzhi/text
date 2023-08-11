package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteWLecturerService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WLecturerAddDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WLecturerPageResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WLecturerPageSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WLecturerUpdateDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author Administrator
 */
public class RemoteWLectureFallbackFactory implements FallbackFactory<RemoteWLecturerService> {


    @Override
    public RemoteWLecturerService create(Throwable cause) {
        return new RemoteWLecturerService() {
            @Override
            public R<PageResultComDTO<WLecturerPageResultDTO>> page(WLecturerPageSearchDTO params) {
                return null;
            }

            @Override
            public R add(WLecturerAddDTO params) {
                return null;
            }

            @Override
            public R delete(IdListRequest request) {
                return null;
            }

            @Override
            public R<Boolean> update(WLecturerUpdateDTO params) {
                return null;
            }
        };
    }
}
