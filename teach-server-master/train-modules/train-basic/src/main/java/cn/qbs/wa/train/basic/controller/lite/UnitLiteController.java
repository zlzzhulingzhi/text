package cn.qbs.wa.train.basic.controller.lite;


import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.train.basic.entity.Unit;
import cn.qbs.wa.train.basic.pojo.unit.*;
import cn.qbs.wa.train.basic.service.UnitService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 单位表(Unit)表控制层
 *
 * @author makejava
 * @since 2022-09-29 08:31:21
 */
@RestController
@RequestMapping("lite/unit")
@Api(tags = "小程序单位管理")
public class UnitLiteController {

    /**
     * 服务对象
     */
    @Resource
    private UnitService unitService;

    /**
     * 小程序查询单位列表
     */
    @PostMapping("appList")
    @ApiOperation("小程序查询单位列表")
    public R<List<AppUnitResponse>> appListUnit() {
        List<Unit> unitList=this.unitService.lambdaQuery().eq(Unit::getEnabled, Constants.DB_TRUE).list();
        List<AppUnitResponse> appUnitResponseList=TreeUtil.copyBeanList(unitList, AppUnitResponse.class);
        return R.ok(appUnitResponseList);
    }

}

