package cn.qbs.wa.teach.basic.api.factory;

import cn.qbs.wa.teach.basic.api.RemoteAppService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.app.ApplicationFullDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.app.ApplicationFullResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteAppFallbackFactory implements FallbackFactory<RemoteAppService> {


    @Override
    public RemoteAppService create(Throwable cause) {
        return new RemoteAppService() {
            @Override
            public R<List<ApplicationFullResultDTO>> getFullList(ApplicationFullDTO applicationFullDTO) {
                return null;
            }
        };
    }
}
