package cn.qbs.wa.train.basic.api.factory;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.api.RemoteTrainAppService;
import cn.qbs.wa.train.basic.api.pojo.DTO.app.ApplicationFullDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.app.ApplicationFullResultDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteTrainAppFallbackFactory implements FallbackFactory<RemoteTrainAppService> {


    @Override
    public RemoteTrainAppService create(Throwable cause) {
        return new RemoteTrainAppService() {
            @Override
            public R<List<ApplicationFullResultDTO>> getFullList(ApplicationFullDTO applicationFullDTO) {
                return null;
            }
        };
    }
}
