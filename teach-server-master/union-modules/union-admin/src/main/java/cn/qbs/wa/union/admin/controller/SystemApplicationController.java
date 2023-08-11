package cn.qbs.wa.union.admin.controller;


import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.union.admin.entity.UniApplication;
import cn.qbs.wa.union.admin.pojo.systemapplication.*;
import cn.qbs.wa.union.admin.service.SystemApplicationService;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.union.admin.service.UniApplicationService;
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
 * 【系统应用】(SystemApplication)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:01
 */
@RestController
@RequestMapping("systemApplication")
@Api(tags = "应用注册")
public class SystemApplicationController {


    /**
     * 服务对象
     */
    @Resource
    private SystemApplicationService systemApplicationService;

    @Resource
    private UniApplicationService uniApplicationService;


    /**
     * 新增【系统应用】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增应用")
    @RequiresPermissions("System:Admin:Application:Register")
    public R<Boolean> add(@RequestBody @Validated SystemApplicationAddRequest params) {
        return R.ok(this.systemApplicationService.add(params));
    }

    /**
     * 分页查询【系统应用】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @RequiresPermissions(value = {"System:Admin:Application:Register", "System:Admin:User"})
    @ApiOperation("分页查询")
    public R<IPage<SystemApplicationPageResponse>> page(@RequestBody SystemApplicationPageRequest params) {
        return R.ok(this.systemApplicationService.page(params));
    }

    /**
     * 查询【系统应用】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @RequiresPermissions("System:Admin:Application:Register")
    @ApiOperation("详情")
    public R<SystemApplicationDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.systemApplicationService.detail(request.getId()));
    }

    /**
     * 查询【系统应用】列表
     *
     * @param id 主键
     * @return
     */
    @PostMapping("list")
    @RequiresPermissions("System:Admin:Application:Register")
    @ApiOperation("列表")
    public R<List<SystemApplicationDetailResponse>> list(@RequestBody SystemApplicationListRequest request) {
        return R.ok(this.systemApplicationService.appList(request));
    }



    /**
     * 修改【系统应用】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @RequiresPermissions("System:Admin:Application:Register")
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated SystemApplicationUpdateRequest params) {
        return R.ok(this.systemApplicationService.update(params));
    }

    /**
     * 删除【系统应用】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("System:Admin:Application:Register")
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        // 检查应用下是否关联系统
        Long count = uniApplicationService.lambdaQuery().in(UniApplication::getApplicationId, request.getIdList()).count();
        if (count != null && count > 0) {
            return R.fail("服务存在应用系统，无法删除");
        }
        return R.ok(this.systemApplicationService.deleteByIds(request.getIdList()));
    }

}

