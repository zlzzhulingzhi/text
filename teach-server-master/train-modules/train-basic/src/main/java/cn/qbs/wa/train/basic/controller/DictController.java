package cn.qbs.wa.train.basic.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.basic.entity.Dict;
import cn.qbs.wa.train.basic.enums.OpenDictCodeEnum;
import cn.qbs.wa.train.basic.pojo.dict.*;
import cn.qbs.wa.train.basic.service.DictService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 字典表(Dict)表控制层
 *
 * @author makejava
 * @since 2021-11-09 19:13:07
 */
@RestController
@RequestMapping("dict")
@Api(tags = "字典管理")
public class DictController {

    /**
     * 服务对象
     */
    @Resource
    private DictService dictService;


    /**
     * 新增字典表
     *
     * @param request
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("System:Management:Plat:Dict:Create")
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated DictAddRequest params) {
        return R.ok(this.dictService.add(params));
    }

    /**
     * 分页查询字典表
     *
     * @param request
     * @return
     */
    @PostMapping("page")
    @RequiresPermissions("System:Management:Plat:Dict")
    //@Log(title = "分页查询字典表", businessType = BusinessType.OTHER)
    @ApiOperation("分页")
    public R<IPage<DictPageResponse>> page(@RequestBody DictPageRequest params) {
        return R.ok(this.dictService.page(params));
    }

    /**
     * 分页查询字典表
     *
     * @param request
     * @return
     */
    @PostMapping("pageByCode")
    //@RequiresPermissions("dict:page")
    //@Log(title = "分页查询字典表", businessType = BusinessType.OTHER)
    @ApiOperation("根据code查询子级字典分页")
    public R<IPage<DictPageResponse>> pageByCode(@RequestBody DictPageRequest params) {
        return R.ok(this.dictService.pageByCode(params));
    }

    /**
     * 查询字典表所有数据
     *
     * @param request
     * @return
     */
    @PostMapping("tree/list")
    //@RequiresPermissions("dict:page")
    @Log(title = "查询字典表树列表", businessType = BusinessType.OTHER)
    @ApiOperation("查询字典表树列表")
    public R<List<TreeDictResponse>> treeList(@RequestBody TreeDictRequest params) {
        return R.ok(this.dictService.treeDict(params));
    }

    /**
     * 查询字典表所有数据
     *
     * @param request
     * @return
     */
    @PostMapping("children/list")
    //@RequiresPermissions("dict:page")
    @Log(title = "查询字典表子列表", businessType = BusinessType.OTHER)
    @ApiOperation("查询字典表子列表")
    public R<List<DictListResponse>> childList(@RequestBody DictChildRequest params) {
        return R.ok(this.dictService.childList(params));
    }

    @PostMapping("children/lists")
    @ApiOperation("查询字典表子列表")
    public R<List<DictListResponse>> childLists(@RequestBody DictChildRequest params) {
        return R.ok(this.dictService.childLists(params));
    }

    @PostMapping("/list")
    @ApiOperation("查询字典表列表")
    public R<List<DictResponse>> dictList(@RequestBody @Validated DictRequest params) {
        List<Dict> list = this.dictService.lambdaQuery()
                .eq(Dict::getCode, params.getCode())
                .ne(Dict::getParentId, 0)
                .eq(params.getEnabled() != null, Dict::getEnabled, params.getEnabled())
                .orderByAsc(Dict::getSort)
                .list();
        return R.ok(BeanUtil.copyToList(list, DictResponse.class));
    }

    /**
     * 查询字典表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("dict:details")
    //@Log(title = "字典表详情", businessType = BusinessType.OTHER)
    @ApiOperation("查询字典想详情")
    public R<DictDetailResponse> detail(@RequestBody IdRequest params) {
        return R.ok(this.dictService.detail(params.getId()));
    }

    /**
     * 修改字典表
     *
     * @param request
     * @return
     */
    @PostMapping("update")
    @RequiresPermissions("System:Management:Plat:Dict:Edit")
    @ApiOperation("更新字典表")
    public R<Boolean> update(@RequestBody @Validated DictUpdateRequest params) {
        return R.ok(this.dictService.update(params));
    }

    /**
     * 删除字典表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("System:Management:Plat:Dict:Delete")
    @ApiOperation("删除字典表")
    public R<Boolean> delete(@RequestBody IdListRequest params) {
        return R.ok(this.dictService.deleteByIds(params.getIdList()));
    }

    /**
     * 批量启用/禁用
     */
    @PostMapping("batch-enabled")
    @RequiresPermissions("System:Management:Plat:Dict:Enabled")
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.dictService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }


    /**
     * 查询字典表所有数据
     *
     * @param params
     * @return
     */
    @PostMapping("/open/tree/list")
    //@RequiresPermissions("dict:page")
    @Log(title = "查询字典表树列表", businessType = BusinessType.OTHER)
    @ApiOperation("查询字典表树列表")
    public R<List<TreeDictResponse>> openDict(@RequestBody TreeDictRequest params) {
        List<TreeDictResponse> list = new ArrayList<>();
        if (OpenDictCodeEnum.getEnumByCode(params.getCode()) != null) {
            list = this.dictService.treeDict(params);
        }
        return R.ok(list);
    }

    @PostMapping("getDictValue")
    @ApiOperation("根据字典码和字典值获取字典名称")
    public R<String> getDictValue(@RequestBody DictPageRequest params) {
        return R.ok(this.dictService.getDictValue(params));
    }

    @PostMapping("getDictKey")
    @ApiOperation("根据字典码和字典名称获取字典值")
    public R<String> getDictKey(@RequestBody DictPageRequest params) {
        return R.ok(this.dictService.getDictKey(params));
    }

}

