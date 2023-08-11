package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteLecturerService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.*;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @author Administrator
 */
public class RemoteLecturerFallbackFactory implements FallbackFactory<RemoteLecturerService> {

    @Override
    public RemoteLecturerService create(Throwable cause) {
        return new RemoteLecturerService() {
            @Override
            public R<List<LecturerDTO>> listLecturers(LecturerSearchDTO search) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<PageResultComDTO<LecturerPageResultDTO>> page(LecturerPageSearchDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<PageResultComDTO<LecturerPageResultDTO>> pageAdmin(LecturerPageSearchDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<LecturerDetailResponseDTO> detail(IdRequest request) {
                return null;
            }
        };
    }
}
