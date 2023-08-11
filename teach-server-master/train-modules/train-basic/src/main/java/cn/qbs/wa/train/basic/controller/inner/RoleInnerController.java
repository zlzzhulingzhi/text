package cn.qbs.wa.train.basic.controller.inner;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.train.basic.entity.Role;
import cn.qbs.wa.train.basic.pojo.role.RoleDetailResponse;
import cn.qbs.wa.train.basic.pojo.role.RoleRequest;
import cn.qbs.wa.train.basic.service.inner.RoleInnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【系统角色】(Role)表控制层
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@RestController
@RequestMapping("/inner/role")
@Api(tags = "角色内部调用接口")
public class RoleInnerController {

    /**
     * 服务对象
     */
    @Resource
    private RoleInnerService roleInnerService;

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("getMenuIds")
    @ApiOperation("获取菜单id")
    public R<List<Long>> getMenuIds(){
        return R.ok(this.roleInnerService.getMenuIds());
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("getRole")
    @ApiOperation("获取角色信息")
    public R<Role> getRole(@RequestBody RoleRequest request) {
        Role role = this.roleInnerService.lambdaQuery().eq(Role::getCode, request.getCode()).eq(Role::getEnabled,
                Constants.DB_TRUE).one();
        return R.ok(role);
    }

    @PostMapping("getRoles")
    @ApiOperation("获取角色信息集合")
    public R<List<RoleDetailResponse>> getRolesByCode(@RequestBody RoleRequest request) {
        if (CollectionUtils.isEmpty(request.getCodes())) {
            throw new ServiceException("角色编码不能为空");
        }
        List<Role> roles = this.roleInnerService.lambdaQuery().in(Role::getCode, request.getCodes()).list();
        return R.ok(TreeUtil.copyBeanList(roles, RoleDetailResponse.class));
    }

}

