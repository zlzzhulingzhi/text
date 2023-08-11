package cn.qbs.wa.teach.course.live.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.api.factory.RemotePlaybackRecordFallbackFactory;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.playbackrecord.BasicPlaybackRecordDetailDTO;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.playbackrecord.BasicPlaybackRecordDetailResultDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 直播业务关系管理
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remotePlaybackRecordService", name = "teach-course-live", path = "/course-live-admin/basicPlaybackRecord", fallbackFactory =  RemotePlaybackRecordFallbackFactory.class)
public interface RemotePlaybackRecordService {


    @PostMapping("detail")
    @ApiOperation("获取直播回放详情")
    R<BasicPlaybackRecordDetailResultDTO> detail(@RequestBody BasicPlaybackRecordDetailDTO basicPlaybackRecordDetailDTO);

}
