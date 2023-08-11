package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.pojo.category.CategoryTreeRequest;
import cn.qbs.wa.teach.organization.pojo.category.CategoryTreeResponse;
import cn.qbs.wa.teach.organization.pojo.groups.*;
import cn.qbs.wa.teach.organization.service.GroupsService;
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
 * 通用分组表(Groups)表控制层
 *
 * @author makejava
 * @since 2022-03-28 09:27:34
 */
@Api(tags = "标签管理")
@RestController
@RequestMapping("groups")
public class GroupsController {

    /**
     * 服务对象
     */
    @Resource
    private GroupsService groupsService;


    /**
     * 创建标签
     *
     * @param params
     * @return
     */
    @ApiOperation("新增标签")
    @PostMapping("add")
    //@RequiresPermissions("groups:add")
    //@Log(title = "新增通用分组表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated GroupsAddRequest params) {
        return R.ok(this.groupsService.add(params));
    }

    /**
     * 查询所有可用标签
     *
     * @return 所有数据
     */
    @ApiOperation("查询所有启用标签")
    @PostMapping("list")
    //@RequiresPermissions("application:list")
    public R<List<GroupsPageResponse>> list() {
        return R.ok(this.groupsService.selectAll());
    }

    /**
     * 查询所有标签
     *
     * @return 所有数据
     */
    @ApiOperation("查询所有标签")
    @PostMapping("listAll")
    //@RequiresPermissions("application:list")
    public R<List<GroupsDetailResponse>> listAll() {
        return R.ok(this.groupsService.selectListAll());
    }

    @PostMapping("tree/list")
    @ApiOperation(value = "树形列表查询")
    public R<List<GroupsTreeResponse>> tree(@RequestBody GroupsTreeRequest params) {
        return R.ok(this.groupsService.tree(params));
    }

    /**
     * 分页查询标签列表
     *
     * @param params
     * @return
     */
    @ApiOperation("标签分页")
    @PostMapping("page")
    //@RequiresPermissions("groups:page")
    //@Log(title = "分页查询通用分组表", businessType = BusinessType.OTHER)
    public R<IPage<GroupsPageResponse>> page(@RequestBody GroupsPageRequest params) {
        return R.ok(this.groupsService.page(params));
    }

    /**
     * 查询标签详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("groups:details")
    //@Log(title = "通用分组表详情", businessType = BusinessType.OTHER)
    public R<GroupsDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.groupsService.detail(request.getId()));
    }

    /**
     * 批量查询标签详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detailList")
    //@RequiresPermissions("groups:details")
    //@Log(title = "通用分组表详情", businessType = BusinessType.OTHER)
    public R<List<GroupsDetailResponse>> detailList(@RequestBody IdListRequest request) {
        return R.ok(this.groupsService.detailList(request.getIdList()));
    }

    /**
     * 修改(禁用,排序)标签
     *
     * @param params
     * @return
     */
    @ApiOperation("修改标签/禁用/排序")
    @PostMapping("update")
    //@RequiresPermissions("groups:update")
    //@Log(title = "更新通用分组表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated GroupsUpdateRequest params) {
        return R.ok(this.groupsService.update(params));
    }

    /**
     * 删除标签
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("groups:delete")
    //@Log(title = "删除通用分组表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.groupsService.deleteByIds(request.getIdList()));
    }

}

