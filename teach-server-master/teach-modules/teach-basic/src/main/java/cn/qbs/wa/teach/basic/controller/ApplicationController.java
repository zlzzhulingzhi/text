package cn.qbs.wa.teach.basic.controller;


import cn.qbs.wa.teach.basic.entity.Application;
import cn.qbs.wa.teach.basic.pojo.app.*;
import cn.qbs.wa.teach.basic.service.ApplicationService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Application)表控制层
 *
 * @author makejava
 * @since 2021-11-02 14:55:27
 */
@RestController
@RequestMapping("app")
@Api(tags = "应用管理")
public class ApplicationController {


    /**
     * 服务对象
     */
    @Resource
    private ApplicationService applicationService;


    /**
     * 新增数据
     *
     * @param application 实体对象
     * @return 新增结果
     */
    @PostMapping("add")
    //@RequiresPermissions("application:add")
    
    @ApiOperation("新增应用")
    public R add(@RequestBody ApplicationAddRequest request) {
        this.applicationService.add(request);
        return R.ok();
    }

    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("page")
    //@RequiresPermissions("application:page")
    @Log(title = "分页查询", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R page(@RequestBody ApplicationPageRequest pageRequest) {
        return R.ok(this.applicationService.pageApplication(pageRequest));
    }


    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("list")
    //@RequiresPermissions("application:list")
    @Log(title = "列表显示", businessType = BusinessType.OTHER)
    @ApiOperation("列表显示")
    public R list() {
        List<Application> list = this.applicationService.list(new LambdaQueryWrapper<Application>().eq(Application::getEnabled, Constants.DB_TRUE).orderByAsc(Application::getSort));
        // 特殊处理，约定应用类型为偶数的 均为平台菜单，统一返回2
        for (Application application : list) {
            if (application.getAppTypeId() > Constants.PLAT_APP_TYPE_ID && application.getAppTypeId() % Constants.PLAT_APP_TYPE_ID == 0) {
                application.setAppTypeId(Constants.PLAT_APP_TYPE_ID);
            }
        }
        return R.ok(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("detail")
    //@RequiresPermissions("application:details")
    @Log(title = "详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R details(@RequestBody IdRequest request) {
        return R.ok(this.applicationService.detail(request.getId()));
    }

    /**
     * 修改
     *
     * @param application 实体对象
     * @return 修改结果
     */
    @PostMapping("update")
    //@RequiresPermissions("application:update")
    @Log(title = "更新", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R update(@RequestBody ApplicationUpdateRequest request) {
        this.applicationService.update(request);
        return R.ok();
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @PostMapping("delete")
    //@RequiresPermissions("application:delete")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R delete(@RequestBody IdListRequest request) {
        this.applicationService.deleteByIds(request.getIdList());
        return R.ok();
    }

    /**
     * 批量启用/禁用
     */
    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.applicationService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }


    @PostMapping("full-list")
    //@RequiresPermissions("role:delete")
    @Log(title = "获取应用菜单包括菜单", businessType = BusinessType.OTHER)
    @ApiOperation("获取应用菜单包括菜单")
    public R<List<ApplicationFullResponse>> getFullList(@RequestBody ApplicationFullRequest request) {
        return R.ok(this.applicationService.getChildrenByAppTypeId(request.getApplicationTypeId()));
    }

}

