package cn.qbs.wa.train.basic.controller.inner;


import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.train.basic.entity.Menu;
import cn.qbs.wa.train.basic.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 【系统菜单】(Menu)表控制层
 *
 * @author makejava
 * @since 2021-11-04 16:28:08
 */
@RestController
@RequestMapping("/inner/menu")
@Api(tags = "菜单内部调用接口")
public class MenuInnerController {

    @Resource
    private MenuService menuService;

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/org-menu")
    @ApiOperation("查询机构所有菜单")
    R<List<Menu>> listOrgMenu() {
        List<Menu> list = menuService.listMenuByAppType(Constants.ORG_APP_TYPE_ID);
        list.removeIf(m -> m.getEnabled().equals(Constants.DB_FAIL));
        return R.ok(list);
    }
}

