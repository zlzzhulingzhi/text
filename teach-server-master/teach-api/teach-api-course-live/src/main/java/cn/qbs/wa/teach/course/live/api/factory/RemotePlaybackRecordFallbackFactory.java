package cn.qbs.wa.teach.course.live.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.api.RemotePlaybackRecordService;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.playbackrecord.BasicPlaybackRecordDetailDTO;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.playbackrecord.BasicPlaybackRecordDetailResultDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemotePlaybackRecordFallbackFactory implements FallbackFactory<RemotePlaybackRecordService> {


    @Override
    public RemotePlaybackRecordService create(Throwable cause) {
        return new RemotePlaybackRecordService() {

            @Override
            public R<BasicPlaybackRecordDetailResultDTO> detail(BasicPlaybackRecordDetailDTO basicPlaybackRecordDetailDTO) {
                return null;
            }
        };
    }
}
