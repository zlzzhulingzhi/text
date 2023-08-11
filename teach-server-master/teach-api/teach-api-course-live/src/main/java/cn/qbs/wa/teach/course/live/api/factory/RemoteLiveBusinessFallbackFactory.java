package cn.qbs.wa.teach.course.live.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.api.RemoteLiveBusinessService;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.livebusiness.LiveBusinessUpdateDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteLiveBusinessFallbackFactory implements FallbackFactory<RemoteLiveBusinessService> {


    @Override
    public RemoteLiveBusinessService create(Throwable cause) {
        return new RemoteLiveBusinessService() {
            @Override
            public R update(LiveBusinessUpdateDTO liveBusinessUpdateDTO) {
                return null;
            }
        };
    }
}
