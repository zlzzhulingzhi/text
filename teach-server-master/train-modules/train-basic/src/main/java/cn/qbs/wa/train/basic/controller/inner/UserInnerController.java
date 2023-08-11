package cn.qbs.wa.train.basic.controller.inner;

import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.train.basic.pojo.user.*;
import cn.qbs.wa.train.basic.service.inner.UserInnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@RestController
@RequestMapping("/inner/user")
@Api(tags = "用户内部调用接口")
public class UserInnerController {

    /**
     * 服务对象
     */
    @Resource
    private UserInnerService userInnerService;

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("org/add")
    @ApiOperation("机构管理新增用户")
    public R addUserOrg(@RequestBody UserOrgAddRequest request) {
        return R.ok(this.userInnerService.saveUserOrg(request));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("org/update")
    @ApiOperation("机构管理更新用户基础信息")
    public R updateOrgUser(@RequestBody UserOrgUpdateRequest request) {
        return R.ok(this.userInnerService.updateUserOrg(request));
    }

}

