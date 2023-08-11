package cn.qbs.wa.teach.organization.controller.inner;

import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.organization.entity.Organization;
import cn.qbs.wa.teach.organization.enums.EnableEnum;
import cn.qbs.wa.teach.organization.enums.OrgCategoryEnum;
import cn.qbs.wa.teach.organization.pojo.organization.OrganizationDetailResponse;
import cn.qbs.wa.teach.organization.pojo.organization.OrganizationPageRequest;
import cn.qbs.wa.teach.organization.pojo.organization.OrganizationPageResponse;
import cn.qbs.wa.teach.organization.pojo.organization.inner.OrganizationInnerAddRequest;
import cn.qbs.wa.teach.organization.pojo.organization.inner.OrganizationInnerUpdateRequest;
import cn.qbs.wa.teach.organization.pojo.organization.inner.OrganizationListInnerDTO;
import cn.qbs.wa.teach.organization.service.OrganizationService;
import cn.qbs.wa.teach.organization.service.inner.OrganizationInnerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 组织机构(Organization)表控制层（仅提供给内部调用）
 *
 * @author wx
 * @since 2022-07-12 13:54:13
 */
@RestController
@RequestMapping("inner/organization")
@Api(tags = "机构管理")
public class OrganizationInnerController {

    /**
     * 服务对象
     */
    @Resource
    private OrganizationService organizationService;

    @Resource
    private OrganizationInnerService organizationInnerService;


    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("init")
    @ApiOperation("初始化组织机构")
    public R<Long> init(@RequestBody OrganizationInnerAddRequest params) {
        return R.ok(this.organizationInnerService.init(params));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("update")
    @ApiOperation("更新组织机构")
    public R<Boolean> update(@RequestBody @Validated OrganizationInnerUpdateRequest params) {
        return R.ok(this.organizationInnerService.update(params));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("detail")
    @ApiOperation("组织机构详情")
    public R<OrganizationDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.organizationInnerService.detail(request.getId()));
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("list")
    @ApiOperation("列表显示")
    public R list(@RequestBody OrganizationListInnerDTO request) {
        return R.ok(this.organizationService.lambdaQuery().select(Organization::getId, Organization::getName, Organization::getCompanyName)
                .eq(Organization::getEnabled, EnableEnum.START.getCode())
                .in(CollectionUtils.isNotEmpty(request.getIds()), Organization::getId, request.getIds())
                .like(StringUtils.isNotBlank(request.getName()), Organization::getName, request.getName())
                .like(StringUtils.isNotBlank(request.getCompanyName()), Organization::getCompanyName, request.getCompanyName())
                .list());
    }


    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("admin/org-dept")
    @ApiOperation("机构部门列表显示")
    public R orgDeptList() {
        return R.ok(this.organizationService.orgDeptList());
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("category/list")
    @Log(title = "类型列表显示", businessType = BusinessType.OTHER)
    @ApiOperation("类型列表显示")
    public R getCategoryList() {
        return R.ok(OrgCategoryEnum.toList());
    }

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("org-dept-tree")
    @ApiOperation("机构部门树形列表")
    public R orgDeptTreeList() {
        return R.ok(this.organizationService.orgDeptTreeList());
    }

    /**
     * 分页查询组织机构
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询组织机构")
    public R<IPage<OrganizationPageResponse>> page(@RequestBody OrganizationPageRequest params) {
        return R.ok(this.organizationService.shopGetOrgPage(params));
    }

}

