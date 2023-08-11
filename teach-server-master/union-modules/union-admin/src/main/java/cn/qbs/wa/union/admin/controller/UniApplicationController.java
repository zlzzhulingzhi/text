package cn.qbs.wa.union.admin.controller;

import cn.qbs.wa.teach.common.security.annotation.Logical;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.union.admin.pojo.uniapplication.*;
import cn.qbs.wa.union.admin.service.UniApplicationService;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 统一应用展示(UniApplication)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
@RestController
@RequestMapping("uniApplication")
@Api(tags = "助手应用")
public class UniApplicationController {

    /**
     * 服务对象
     */
    @Resource
    private UniApplicationService uniApplicationService;

    /**
     * 新增统一应用展示
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("System:Admin:Application:Helper")
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated UniApplicationAddRequest params) {
        return R.ok(this.uniApplicationService.add(params));
    }

    /**
     * 分页查询统一应用展示
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @RequiresPermissions(value = {"System:Admin:Application:Helper", "System:Admin:User"}, logical = Logical.OR)
    @ApiOperation("分页")
    public R<IPage<UniApplicationPageResponse>> page(@RequestBody UniApplicationPageRequest params) {
        return R.ok(this.uniApplicationService.page(params));
    }

    /**
     * 查询统一应用展示详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @RequiresPermissions("System:Admin:Application:Helper")
    @ApiOperation("详情")
    public R<UniApplicationDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.uniApplicationService.detail(request.getId()));
    }

    /**
     * 修改统一应用展示
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @RequiresPermissions("System:Admin:Application:Helper")
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated UniApplicationUpdateRequest params) {
        return R.ok(this.uniApplicationService.update(params));
    }

    /**
     * 删除统一应用展示
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("System:Admin:Application:Helper")
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.uniApplicationService.deleteByIds(request.getIdList()));
    }

}

