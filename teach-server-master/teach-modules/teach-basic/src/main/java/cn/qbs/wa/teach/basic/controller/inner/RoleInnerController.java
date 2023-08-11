package cn.qbs.wa.teach.basic.controller.inner;

import cn.qbs.wa.teach.basic.service.inner.RoleInnerService;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
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



}

