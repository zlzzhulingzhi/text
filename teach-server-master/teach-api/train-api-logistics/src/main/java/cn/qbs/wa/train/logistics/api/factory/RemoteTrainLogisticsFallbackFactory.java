package cn.qbs.wa.train.logistics.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.RemoteTrainLogisticsService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.OrganizationAddRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.OrganizationUpdateRequestDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author Administrator
 */
public class RemoteTrainLogisticsFallbackFactory implements FallbackFactory<RemoteTrainLogisticsService> {


    @Override
    public RemoteTrainLogisticsService create(Throwable cause) {
        return new RemoteTrainLogisticsService() {

            @Override
            public R<Boolean> init(OrganizationAddRequestDTO params) {
                return null;
            }

            @Override
            public R<Boolean> adminUpdate(OrganizationUpdateRequestDTO params) {
                return null;
            }

            @Override
            public R<Boolean> update(OrganizationUpdateRequestDTO params) {
                return null;
            }
        };
    }
}
