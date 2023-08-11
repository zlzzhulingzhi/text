package cn.qbs.wa.train.basic.controller.lite;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.pojo.clockinconfig.ClockInConfigDetailResponse;
import cn.qbs.wa.train.basic.pojo.clockinconfig.ClockInConfigListRequest;
import cn.qbs.wa.train.basic.service.ClockInConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 打卡配置(ClockInConfig)表控制层
 *
 * @author makejava
 * @since 2022-12-26 15:38:20
 */
@RestController
@RequestMapping("clockInConfig")
@Api(tags = "小程序学员打卡配置")
public class ClockInConfigLiteController {

    /**
     * 服务对象
     */
    @Resource
    private ClockInConfigService clockInConfigService;



    /**
     * 查询打卡配置详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("list")
    //@RequiresPermissions("clockInConfig:details")
    //@Log(title = "打卡配置详情", businessType = BusinessType.OTHER)
    @ApiOperation("打卡配置列表")
    public R<List<ClockInConfigDetailResponse>> configList(@RequestBody ClockInConfigListRequest request) {
        return R.ok(this.clockInConfigService.configList(request));
    }



}

