package cn.qbs.wa.train.screen.controller;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.screen.pojo.permission.MenuPermissionResponse;
import cn.qbs.wa.train.screen.service.SystemMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "系统接口")
@RestController
public class IndexController {

    @Resource
    private SystemMenuService systemMenuService;

    @PostMapping("/menu-permission")
    @ApiOperation("菜单权限")
    public R<MenuPermissionResponse> getMenuPermission() {
        return R.ok(systemMenuService.getMenuPermission());
    }

    @PostMapping("/video/transcoding")
    public R<Void> videoTranscoding(@RequestBody String xmlData) {
        log.info("视频转码回调: ");
        log.info(xmlData);
        return R.ok();
    }
}
