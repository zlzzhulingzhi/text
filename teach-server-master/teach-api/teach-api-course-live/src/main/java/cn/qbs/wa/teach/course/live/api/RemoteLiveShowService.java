package cn.qbs.wa.teach.course.live.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.api.factory.RemoteLiveShowFallbackFactory;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.liveshow.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 直播场次管理
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteLiveShowService", name = "teach-course-live", path = "/course-live-admin/basicLiveShow", fallbackFactory = RemoteLiveShowFallbackFactory.class)
public interface RemoteLiveShowService {

    /**
     * 新增基础直播场次表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增基础直播场次表")
    R<LiveShowAddResultDTO> add(@RequestBody LiveShowAddDTO liveShowAddDTO);

    /**
     * 新增基础直播场次表
     *
     * @param params
     * @return
     */
    @PostMapping("add-monitor")
    @ApiOperation("新增监考直播场次表")
    R<LiveShowAddResultDTO> addMonitor(@RequestBody LiveShowMonitorAddDTO liveShowMonitorAddDTO);

    /**
     * 直播信息获取
     *
     * @param params
     * @return
     */
    @PostMapping("info")
    @ApiOperation("直播信息获取")
    R<BasicLiveShowInfoResultDTO> getInfo(@RequestBody BasicLiveShowInfoDTO basicLiveShowInfoDTO);


    /**
     * 获取用户签名
     *
     * @param params
     * @return
     */
    @PostMapping("/userSig")
    @ApiOperation(value = "获取直播用户签名")
    R<String> getLiveShowUserSig(@RequestBody LiveShowUserSigDTO liveShowUserSigDTO);

}
