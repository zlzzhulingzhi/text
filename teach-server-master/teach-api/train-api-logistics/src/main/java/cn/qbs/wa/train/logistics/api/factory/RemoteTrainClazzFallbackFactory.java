package cn.qbs.wa.train.logistics.api.factory;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.RemoteTrainClazzService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzPageRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzPageResponseDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.IntegrateClazzResponse;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public class RemoteTrainClazzFallbackFactory implements FallbackFactory<RemoteTrainClazzService> {


    @Override
    public RemoteTrainClazzService create(Throwable cause) {
        return new RemoteTrainClazzService() {

            @Override
            public R<List<ClazzPageResponseDTO>> getByOrgId(ClazzPageRequestDTO params) {
                return null;
            }

            @Override
            public R<Long> getCountByCourseId(ClazzPageRequestDTO params) {
                return null;
            }

            @Override
            public R<List<IntegrateClazzResponse>> listClazzByMemberId(IdRequest request) {
                return null;
            }

            @Override
            public R<Map<Long, String>> queryClazzLastByLecturerIds(IdListRequest request) {
                return null;
            }

            @Override
            public R<List<IntegrateClazzResponse>> listClazzByLecturerId(IdRequest request) {
                return null;
            }
        };
    }
}
