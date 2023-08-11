package cn.qbs.wa.train.logistics.controller;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.enums.OrgCategoryEnum;
import cn.qbs.wa.train.logistics.pojo.organization.*;
import cn.qbs.wa.train.logistics.service.OrganizationService;
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
 * 组织机构(Organization)表控制层
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
@RestController
@RequestMapping("organization")
@Api(tags = "机构管理")
public class OrganizationController {

    /**
     * 培训中心新增机构后教务中心同步增加
     */
    @Resource
    private OrganizationService organizationService;

    /**
     * 新增组织机构
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("organization:add")
    //@Log(title = "新增组织机构", businessType = BusinessType.INSERT)
    @ApiOperation("新增组织机构")
    public R<Boolean> add(@RequestBody @Validated OrganizationAddRequest params) {
        return R.ok(this.organizationService.add(params));
    }

    /**
     * 分页查询组织机构
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("organization:page")
    //@Log(title = "分页查询组织机构", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询组织机构")
    public R<IPage<OrganizationPageResponse>> page(@RequestBody OrganizationPageRequest params) {
        return R.ok(this.organizationService.page(params));
    }

    /**
     * 查询组织机构详情
     * @param request 主键
     * @return
     */
    @PostMapping("/info")
    //@RequiresPermissions("organization:details")
    //@Log(title = "组织机构详情", businessType = BusinessType.OTHER)
    @ApiOperation("机构详情")
    public R<Organization> info(@RequestBody @Validated IdRequest request) {
        return R.ok(this.organizationService.getById(request.getId()));
    }

    /**
     * 查询组织机构详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("admin/detail")
    //@RequiresPermissions("organization:details")
    //@Log(title = "组织机构详情", businessType = BusinessType.OTHER)
    @ApiOperation("管理员组织机构详情")
    public R<OrganizationDetailResponse> adminDetail(@RequestBody IdRequest request) {
        SecurityContextHolder.setSelectOrgId(request.getId().toString());
        return R.ok(this.organizationService.detail(request.getId()));
    }

    /**
     * 查询组织机构详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("organization:details")
    //@Log(title = "组织机构详情", businessType = BusinessType.OTHER)
    @ApiOperation("组织机构详情")
    public R<OrganizationDetailResponse> detail(@RequestBody IdRequest request) {
        request.setId(SecurityUtils.getLoginUser().getOrgId());
        return R.ok(this.organizationService.detail(request.getId()));
    }

    /**
     * 修改组织机构
     *
     * @param params
     * @return
     */
    @PostMapping("/admin/update")
    //@RequiresPermissions("organization:update")
    //@Log(title = "更新组织机构", businessType = BusinessType.UPDATE)
    @ApiOperation("更新组织机构")
    public R<Boolean> adminUpdate(@RequestBody @Validated OrganizationUpdateRequest params) {
        if(params.getId()==null){
            params.setId(SecurityContextHolder.getOrgId());
            SecurityContextHolder.setSelectOrgId(SecurityContextHolder.getOrgId().toString());
        }else{
            SecurityContextHolder.setSelectOrgId(params.getId().toString());
        }
        return R.ok(this.organizationService.adminUpdate(params));
    }

    /**
     * 修改组织机构
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("organization:update")
    //@Log(title = "更新组织机构", businessType = BusinessType.UPDATE)
    @ApiOperation("更新组织机构")
    public R<Boolean> update(@RequestBody @Validated OrganizationUpdateRequest params) {
        if(params.getId()==null){
            params.setId(SecurityContextHolder.getOrgId());
            SecurityContextHolder.setSelectOrgId(SecurityContextHolder.getOrgId().toString());
        }else{
            SecurityContextHolder.setSelectOrgId(params.getId().toString());
        }
        return R.ok(this.organizationService.update(params));
    }

    /**
     * 删除组织机构
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("organization:delete")
    //@Log(title = "删除组织机构", businessType = BusinessType.DELETE)
    @ApiOperation("删除组织机构")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.organizationService.deleteByIds(request.getIdList()));
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("list")
    //@RequiresPermissions("application:list")
    @ApiOperation("列表显示")
    public R list() {
        return R.ok(this.organizationService.listOrg());
    }


    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("admin/org-dept")
    //@RequiresPermissions("application:list")
    @ApiOperation("机构部门列表显示")
    public R orgDeptList() {
        return R.ok(this.organizationService.orgDeptList());
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("category/list")
    //@RequiresPermissions("application:list")
    @ApiOperation("类型列表显示")
    public R getCategoryList() {
        return R.ok(OrgCategoryEnum.toList());
    }


    /**
     * 批量启用/禁用
     */
    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.organizationService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }


    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @PostMapping("org-dept-tree")
    //@RequiresPermissions("application:org-dept-list")
    @ApiOperation("机构部门树形列表")
    public R orgDeptTreeList() {
        return R.ok(this.organizationService.orgDeptTreeList());
    }

}

