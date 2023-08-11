package cn.qbs.wa.teach.basic.controller;


import cn.qbs.wa.teach.basic.entity.ApplicationType;
import cn.qbs.wa.teach.basic.pojo.applicationtype.*;
import cn.qbs.wa.teach.basic.service.ApplicationTypeService;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * (ApplicationType)表控制层
 *
 * @author makejava
 * @since 2021-11-09 19:14:28
 */
@RestController
@RequestMapping("applicationType")
@Api(tags = "应用类型")
public class ApplicationTypeController {

    /**
     * 服务对象
     */
    @Resource
    private ApplicationTypeService applicationTypeService;


    /**
     * 新增
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("applicationType:add")
    //
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated ApplicationTypeAddRequest params) {
        return R.ok(this.applicationTypeService.add(params));
    }

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("applicationType:page")
    //@Log(title = "分页查询", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R<IPage<ApplicationTypePageResponse>> page(@RequestBody ApplicationTypePageRequest params) {
        return R.ok(this.applicationTypeService.page(params));
    }


    /**
     * 列表查询
     */
    @PostMapping("list")
    //@RequiresPermissions("applicationType:add")
    //
    @ApiOperation("应用类型列表")
    public R list(@RequestBody IdRequest request) {
        return R.ok(this.applicationTypeService.list(new LambdaQueryWrapper<ApplicationType>().eq(ApplicationType::getParentId,request.getId())));
    }

    /**
     * 查询详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("applicationType:details")
    //@Log(title = "详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<ApplicationTypeDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.applicationTypeService.detail(request.getId()));
    }

    /**
     * 修改
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("applicationType:update")
    //@Log(title = "更新", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated ApplicationTypeUpdateRequest params) {
        return R.ok(this.applicationTypeService.update(params));
    }

    /**
     * 删除
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("applicationType:delete")
    //@Log(title = "删除", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.applicationTypeService.deleteByIds(request.getIdList()));
    }

}

