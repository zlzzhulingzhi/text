package cn.qbs.wa.teach.course.standard.controller.platform;

import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.*;
import cn.qbs.wa.teach.course.standard.service.TBannerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;

/**
 * Banner图片(TBanner)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-12-22 14:01:03
 */
@Api(tags = "小程序-banner图管理")
@RestController
@RequestMapping("/banner")
public class BannerController {

    @Resource
    private TBannerService tBannerService;

    /**
     * 新增Banner图片
     */
    @ApiOperation("新增")
    @PostMapping("/add")
    @RequiresPermissions("Manage:Banner")
    public R<Boolean> add(@RequestBody @Validated TBannerAddRequest params) {
        return R.ok(this.tBannerService.add(params));
    }
    
    /**
     * 分页查询Banner图片
     */
    @ApiOperation("分页查询")
    @PostMapping("/page")
    @RequiresPermissions("Manage:Banner")
    public R<IPage<TBannerResponse>> page(@RequestBody TBannerPageRequest params) {
        return R.ok(this.tBannerService.page(params));
    }

    /**
     * 修改Banner图片
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @RequiresPermissions("Manage:Banner")
    public R<Boolean> update(@RequestBody @Validated TBannerUpdateRequest params) {
        return R.ok(this.tBannerService.update(params));
    }

    /**
     * 删除Banner图片
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @RequiresPermissions("Manage:Banner")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.tBannerService.deleteByIds(request.getIdList()));
    }
    
}

