package cn.qbs.wa.train.logistics.controller.inner;

import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.train.logistics.pojo.organization.OrganizationUpdateRequest;
import cn.qbs.wa.train.logistics.pojo.organization.inner.OrganizationInnerAddRequest;
import cn.qbs.wa.train.logistics.service.inner.OrganizationInnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "机构内部调用接口")
public class OrganizationInnerController {

    @Resource
    private OrganizationInnerService organizationInnerService;

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("update")
    @ApiOperation("更新组织机构")
    public R<Boolean> update(@RequestBody OrganizationUpdateRequest params) {
        return R.ok(this.organizationInnerService.update(params));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/admin/update")
    @ApiOperation("更新组织机构")
    public R<Boolean> adminUpdate(@RequestBody OrganizationUpdateRequest params) {
        return R.ok(this.organizationInnerService.adminUpdate(params));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("init")
    @ApiOperation("初始化组织机构")
    public R<Boolean> init(@RequestBody OrganizationInnerAddRequest params) {
        return R.ok(this.organizationInnerService.init(params));
    }

}

