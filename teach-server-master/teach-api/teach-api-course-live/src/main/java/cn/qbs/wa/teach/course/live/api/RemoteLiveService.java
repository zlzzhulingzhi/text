package cn.qbs.wa.teach.course.live.api;

import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.api.factory.RemoteLiveFallbackFactory;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.live.LiveListDTO;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.live.LivePageDTO;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.live.LiveResultDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *  基础直播管理
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteLiveService", name = "teach-course-live", path = "/course-live-admin/basicLive", fallbackFactory = RemoteLiveFallbackFactory.class)
public interface RemoteLiveService {

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/page")
    @ApiOperation("直播分页列表")
    R<PageResultComDTO<LiveResultDTO>> page(@RequestBody LivePageDTO request);

    /**
     * 查询基础直播详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("list")
    @ApiOperation("直播列表")
    R<List<LiveResultDTO>> list(@RequestBody LiveListDTO request) ;

    /**
     * 商城远程获取直播列表
     *
     * @param id 主键
     * @return
     */
    @PostMapping("shopGetList")
    @ApiOperation("直播列表")
    R<List<LiveResultDTO>> shopGetList(@RequestBody LiveListDTO request) ;

    @PostMapping("prepare/page")
    @ApiOperation("准备直播分页")
    R<PageResultComDTO<LiveResultDTO>> preparePage(@RequestBody LivePageDTO request);

    @PostMapping("prepare/page/V2")
    @ApiOperation("准备直播分页")
    R<PageResultComDTO<LiveResultDTO>> preparePageV2(@RequestBody LivePageDTO request);



}
