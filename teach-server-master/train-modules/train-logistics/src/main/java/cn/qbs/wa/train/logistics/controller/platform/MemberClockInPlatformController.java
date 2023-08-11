package cn.qbs.wa.train.logistics.controller.platform;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.pojo.memberclockin.MemberClockInPageRequest;
import cn.qbs.wa.train.logistics.pojo.memberclockin.MemberClockInPageResponse;
import cn.qbs.wa.train.logistics.service.MemberClockInService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 学员打卡记录(MemberClockIn)表控制层
 *
 * @author makejava
 * @since 2022-12-26 15:42:22
 */
@RestController
@RequestMapping("plat/memberClockIn")
@Api(tags = "平台打卡管理")
public class MemberClockInPlatformController {

    /**
     * 服务对象
     */
    @Resource
    private MemberClockInService memberClockInService;




    /**
     * 分页查询学员打卡记录
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("memberClockIn:page")
    //@Log(title = "分页查询学员打卡记录", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询学员打卡记录")
    public R<IPage<MemberClockInPageResponse>> page(@RequestBody @Validated MemberClockInPageRequest params) {
        return R.ok(this.memberClockInService.page(params));
    }



}

