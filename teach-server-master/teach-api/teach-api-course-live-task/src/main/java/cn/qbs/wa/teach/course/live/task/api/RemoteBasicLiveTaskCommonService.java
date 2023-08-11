package cn.qbs.wa.teach.course.live.task.api;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.task.api.factory.RemotePlaybackRecordCallBackFallbackFactory;
import cn.qbs.wa.teach.course.live.task.api.pojo.DTO.BasicLiveConfigJobAddDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "remoteBasicLiveTaskCommonService", name = "teach-course-live-task", path = "/course-live-task/basicCommon", fallbackFactory = RemotePlaybackRecordCallBackFallbackFactory.class)
public interface RemoteBasicLiveTaskCommonService {


    @PostMapping("job/notice")
    R createNoticeJob(@RequestBody BasicLiveConfigJobAddDTO params);


}
