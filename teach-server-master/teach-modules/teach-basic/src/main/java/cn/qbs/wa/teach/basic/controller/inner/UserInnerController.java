package cn.qbs.wa.teach.basic.controller.inner;


import cn.qbs.wa.teach.basic.pojo.user.*;
import cn.qbs.wa.teach.basic.service.inner.UserInnerService;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


@RestController
@RequestMapping("user/inner")
@Api(tags = "用户管理")
public class UserInnerController {

    @Resource
    private UserInnerService userInnerService;

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("org/add")
    @ApiOperation("机构管理新增用户")
    public R addUserOrg(@RequestBody @Validated UserOrgAddRequest request) {
        return R.ok(this.userInnerService.saveUserOrg(request));
    }

}

