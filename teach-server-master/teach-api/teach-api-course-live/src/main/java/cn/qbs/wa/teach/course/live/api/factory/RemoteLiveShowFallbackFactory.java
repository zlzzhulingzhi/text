package cn.qbs.wa.teach.course.live.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.api.RemoteLiveShowService;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.liveshow.*;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteLiveShowFallbackFactory implements FallbackFactory<RemoteLiveShowService> {


    @Override
    public RemoteLiveShowService create(Throwable cause) {
        return new RemoteLiveShowService() {
            @Override
            public R<LiveShowAddResultDTO> add(LiveShowAddDTO liveShowAddDTO) {
                return null;
            }

            @Override
            public R<LiveShowAddResultDTO> addMonitor(LiveShowMonitorAddDTO liveShowMonitorAddDTO) {
                return null;
            }

            @Override
            public R<BasicLiveShowInfoResultDTO> getInfo(BasicLiveShowInfoDTO basicLiveShowInfoDTO) {
                return null;
            }

            @Override
            public R<String> getLiveShowUserSig(LiveShowUserSigDTO liveShowUserSigDTO) {
                return null;
            }
        };
    }
}
