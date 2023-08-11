package cn.qbs.wa.union.admin.controller;


import cn.qbs.wa.union.admin.pojo.uniapplicationtype.*;
import cn.qbs.wa.union.admin.service.UniApplicationTypeService;
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
import java.util.List;


/**
 * 统一应用类型(UniApplicationType)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@RestController
@RequestMapping("uniApplicationType")
@Api(tags = "助手应用分类")
public class UniApplicationTypeController {

    /**
     * 服务对象
     */
    @Resource
    private UniApplicationTypeService uniApplicationTypeService;


    /**
     * 新增统一应用类型
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("uniApplicationType:add")
    //@Log(title = "新增统一应用类型", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated UniApplicationTypeAddRequest params) {
        return R.ok(this.uniApplicationTypeService.add(params));
    }

    @PostMapping("tree")
    //@RequiresPermissions("menu:page")
    //@Log(title = "分页查询【系统菜单】", businessType = BusinessType.OTHER)
    @ApiOperation("树列表")
    public R<List<UniApplicationTypeTreeResponse>> getTreeList(@RequestBody UniApplicationTypeTreeRequest request) {
        return R.ok(this.uniApplicationTypeService.getTreeList(request));
    }

    /**
     * 分页查询统一应用类型
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("uniApplicationType:page")
    //@Log(title = "分页查询统一应用类型", businessType = BusinessType.OTHER)
    public R<IPage<UniApplicationTypePageResponse>> page(@RequestBody UniApplicationTypePageRequest params) {
        return R.ok(this.uniApplicationTypeService.page(params));
    }

    /**
     * 查询统一应用类型详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("uniApplicationType:details")
    //@Log(title = "统一应用类型详情", businessType = BusinessType.OTHER)
    public R<UniApplicationTypeDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.uniApplicationTypeService.detail(request.getId()));
    }

    /**
     * 修改统一应用类型
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("uniApplicationType:update")
    //@Log(title = "更新统一应用类型", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated UniApplicationTypeUpdateRequest params) {
        return R.ok(this.uniApplicationTypeService.update(params));
    }

    /**
     * 删除统一应用类型
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("uniApplicationType:delete")
    //@Log(title = "删除统一应用类型", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.uniApplicationTypeService.deleteByIds(request.getIdList()));
    }

}

