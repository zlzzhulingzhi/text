package cn.qbs.wa.train.logistics.controller.platform;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.entity.Clazz;
import cn.qbs.wa.train.logistics.entity.SceneDevice;
import cn.qbs.wa.train.logistics.pojo.scenedevice.*;
import cn.qbs.wa.train.logistics.service.platform.SceneDeviceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 场景设备(SceneDevice)表控制层
 *
 * @author makejava
 * @since 2022-10-13 16:03:01
 */
@RestController
@RequestMapping("sceneDevice")
@Api(tags = "场景设备管理")
public class SceneDeviceController {

    /**
     * 服务对象
     */
    @Resource
    private SceneDeviceService sceneDeviceService;


    /**
     * 新增场景设备
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增场场景设备")
    @RequiresPermissions("Management:Class:Device")
    //@Log(title = "新增场景设备", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated SceneDeviceAddRequest params) {
        return R.ok(this.sceneDeviceService.add(params));
    }

    /**
     * 分页查询场景设备
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询场景设备")
    @RequiresPermissions("Management:Class:Device")
    //@Log(title = "分页查询场景设备", businessType = BusinessType.OTHER)
    public R<IPage<SceneDevicePageResponse>> page(@RequestBody SceneDevicePageRequest params) {
        return R.ok(this.sceneDeviceService.page(params));
    }

    /**
     * 查询场景设备详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("查询场景设备详情")
    @RequiresPermissions("Management:Class:Device")
    //@Log(title = "场景设备详情", businessType = BusinessType.OTHER)
    public R<SceneDeviceDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.sceneDeviceService.detail(request.getId()));
    }

    /**
     * 修改场景设备
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("修改场景设备")
    @RequiresPermissions("Management:Class:Device")
    //@Log(title = "更新场景设备", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated SceneDeviceUpdateRequest params) {
        return R.ok(this.sceneDeviceService.update(params));
    }

    @PostMapping("updateBatch")
    @ApiOperation("批量修改场景设备")
    //@RequiresPermissions("sceneDevice:update")
    //@Log(title = "更新场景设备", businessType = BusinessType.UPDATE)
    public R<Boolean> updateBatch(@RequestBody @Validated SceneDeviceUpdateBatchRequest params) {
        return R.ok(this.sceneDeviceService.updateBatch(params));
    }

    /**
     * 删除场景设备
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除场景设备")
    @RequiresPermissions("Management:Class:Device")
    //@Log(title = "删除场景设备", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.sceneDeviceService.deleteByIds(request.getIdList()));
    }

    @PostMapping("list")
    @ApiOperation("查询场景设备列表")
    public R<List<SceneDevice>> listSceneDevice(@RequestBody SceneDevicePageRequest request) {
        return R.ok(this.sceneDeviceService.listSceneDevice(request));
    }

}

