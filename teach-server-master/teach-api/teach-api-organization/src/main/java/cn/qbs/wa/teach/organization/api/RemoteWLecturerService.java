package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteWLectureFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WLecturerAddDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WLecturerPageResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WLecturerPageSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer.WLecturerUpdateDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteWLecturerService", name = "teach-org", path = "org/WLecturer", fallbackFactory = RemoteWLectureFallbackFactory.class)
public interface RemoteWLecturerService {


    /**
     * 分页查询机构插件表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询机构插件表")
    R<PageResultComDTO<WLecturerPageResultDTO>> page(@RequestBody WLecturerPageSearchDTO params);

    /**
     * 新增插件-讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增插件-讲师表")
    R add(@RequestBody @Validated WLecturerAddDTO params);

    /**
     * 删除插件-讲师表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除插件-讲师表")
    R delete(@RequestBody IdListRequest request);

    /**
     * 修改插件-讲师表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("wLecturer:update")
    @ApiOperation("更新插件-讲师表")
     R<Boolean> update(@RequestBody @Validated WLecturerUpdateDTO params) ;


}
