package cn.qbs.wa.teach.course.live.task.api;


import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.task.api.factory.RemotePlaybackRecordCallBackFallbackFactory;
import cn.qbs.wa.teach.course.live.task.api.pojo.DTO.BasicPlaybackRecordTencentAddDTO;
import cn.qbs.wa.teach.course.live.task.api.pojo.DTO.WhiteBoardCallBackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "remotePlaybackRecordCallBackService", name = "teach-course-live-task", path = "/course-live-task/basicPlaybackRecord", fallbackFactory = RemotePlaybackRecordCallBackFallbackFactory.class)
public interface RemotePlayBackRecordCallBackService {

    @PostMapping("add")
    R add(@RequestBody @Validated BasicPlaybackRecordTencentAddDTO params);

    @PostMapping("course/reset")
    R courseReset(@RequestBody IdRequest params);

    @PostMapping("board/callBack")
    R updateBoardStream(@RequestBody WhiteBoardCallBackDTO request);
}
