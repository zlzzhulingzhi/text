package cn.qbs.wa.train.basic.controller.inner;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.pojo.clockinconfig.*;
import cn.qbs.wa.train.basic.service.ClockInConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 打卡配置(ClockInConfig)表控制层
 *
 * @author makejava
 * @since 2022-12-26 15:38:20
 */
@RestController
@RequestMapping("clockInConfig")
public class ClockInConfigInnerController {

    /**
     * 服务对象
     */
    @Resource
    private ClockInConfigService clockInConfigService;


    /**
     * 新增打卡配置
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("clockInConfig:add")
    //@Log(title = "新增打卡配置", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated ClockInConfigAddRequest params) {
        return R.ok(this.clockInConfigService.add(params));
    }

    /**
     * 分页查询打卡配置
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("clockInConfig:page")
    //@Log(title = "分页查询打卡配置", businessType = BusinessType.OTHER)
    public R<IPage<ClockInConfigPageResponse>> page(@RequestBody ClockInConfigPageRequest params) {
        return R.ok(this.clockInConfigService.page(params));
    }

    /**
     * 查询打卡配置详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("clockInConfig:details")
    //@Log(title = "打卡配置详情", businessType = BusinessType.OTHER)
    public R<ClockInConfigDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.clockInConfigService.detail(request.getId()));
    }

    /**
     * 修改打卡配置
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("clockInConfig:update")
    //@Log(title = "更新打卡配置", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ClockInConfigUpdateRequest params) {
        return R.ok(this.clockInConfigService.update(params));
    }

    /**
     * 删除打卡配置
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("clockInConfig:delete")
    //@Log(title = "删除打卡配置", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.clockInConfigService.deleteByIds(request.getIdList()));
    }

}

