package cn.qbs.wa.union.admin.controller;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.Logical;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.union.admin.entity.SystemRole;
import cn.qbs.wa.union.admin.pojo.systemrole.*;
import cn.qbs.wa.union.admin.service.SystemRoleService;
import cn.qbs.wa.union.admin.service.SystemUserRoleService;
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
import java.util.List;


/**
 * 【系统角色】(SystemRole)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
@RestController
@RequestMapping("systemRole")
@Api(tags = "角色管理")
public class SystemRoleController {

    /**
     * 服务对象
     */
    @Resource
    private SystemRoleService systemRoleService;

    @Resource
    private SystemUserRoleService systemUserRoleService;

    /**
     * 新增【系统角色】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("System:Admin:Role")
    @ApiOperation("平台管理新增角色")
    public R<Boolean> add(@RequestBody @Validated SystemRoleAddRequest params) {
        return R.ok(this.systemRoleService.add(params));
    }

    /**
     * 分页查询【系统角色】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @RequiresPermissions(value = {"System:Admin:Role", "System:Admin:User"}, logical = Logical.OR)
    @ApiOperation("分页查询")
    public R<IPage<SystemRolePageResponse>> page(@RequestBody SystemRolePageRequest params) {
        return R.ok(this.systemRoleService.page(params));
    }

    /**
     * 查询【系统角色】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @RequiresPermissions("System:Admin:Role")
    public R<SystemRoleDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.systemRoleService.detail(request.getId()));
    }

    /**
     * 修改【系统角色】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @RequiresPermissions("System:Admin:Role")
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated SystemRoleUpdateRequest params) {
        return R.ok(this.systemRoleService.update(params));
    }


    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("list")
    @RequiresPermissions(value = {"System:Admin:Role", "System:Admin:User"}, logical = Logical.OR)
    @ApiOperation("列表查询")
    public R list() {
        List<SystemRole> list = this.systemRoleService.list(new LambdaQueryWrapper<SystemRole>().eq(SystemRole::getEnabled, Constants.DB_TRUE));
        // 不能选择比当前操作人员角色高的角色
        systemUserRoleService.filterInvalid(list);
        return R.ok(list);
    }

    /**
     * 删除【系统角色】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("System:Admin:Role")
    @ApiOperation("角色删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.systemRoleService.deleteByIds(request.getIdList()));
    }

}

