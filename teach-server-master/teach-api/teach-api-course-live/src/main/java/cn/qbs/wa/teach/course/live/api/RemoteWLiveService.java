package cn.qbs.wa.teach.course.live.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.api.factory.RemoteWLiveFallbackFactory;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive.WLiveAddDTO;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive.WLiveDeleteByBusinessDTO;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive.WLivePageResultDTO;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.wlive.WLivePageSearchDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteWLiveService", name = "teach-course-live", path = "course-live-admin/WLive", fallbackFactory = RemoteWLiveFallbackFactory.class)
public interface RemoteWLiveService {


    /**
     * 分页查询机构插件表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询插件表")
    R<PageResultComDTO<WLivePageResultDTO>> page(@RequestBody WLivePageSearchDTO params);

    /**
     * 新增插件-讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增插件-直播表")
    R add(@RequestBody @Validated WLiveAddDTO params);

    /**
     * 删除插件-讲师表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除插件-直播表")
    R delete(@RequestBody IdListRequest request);

    /**
     * 删除基础直播业务关联表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete-business")
    //@RequiresPermissions("wLive:delete")
    //@Log(title = "删除基础直播业务关联表", businessType = BusinessType.DELETE)
    @ApiOperation("删除插件-直播表")
    R deleteByBusiness(@RequestBody @Validated WLiveDeleteByBusinessDTO request) ;


}
