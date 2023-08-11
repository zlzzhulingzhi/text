package cn.qbs.wa.train.logistics.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.RemoteTrainClazzStudentService;
import cn.qbs.wa.train.logistics.api.RemoteTrainLogisticsService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzStudentPageRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzStudentPageResponseDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.OrganizationAddRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.OrganizationUpdateRequestDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public class RemoteTrainClazzStudentFallbackFactory implements FallbackFactory<RemoteTrainClazzStudentService> {


    @Override
    public RemoteTrainClazzStudentService create(Throwable cause) {
        return new RemoteTrainClazzStudentService() {


            @Override
            public R<List<ClazzStudentPageResponseDTO>> getByOrgId(ClazzStudentPageRequestDTO params) {
                return null;
            }

            @Override
            public R<Map<Long, String>> queryClazzLast(IdListRequest request) {
                return null;
            }
        };
    }
}
