package cn.qbs.wa.train.logistics.controller.platform;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.MatterReport;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.pojo.matter.*;
import cn.qbs.wa.train.logistics.service.MatterReportService;
import cn.qbs.wa.train.logistics.service.lite.LiteLogisticsService;
import cn.qbs.wa.train.logistics.service.platform.MatterReportPlatService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author yjx
 */
@RestController
@RequestMapping("/plat")
@Api(tags = "报事报修")
public class MatterReportPlatController {

    @Resource
    private MatterReportPlatService matterReportPlatService;

    @PostMapping("/matter-report/org/list")
    @ApiOperation("报事报修-机构列表")
    public R<List<Organization>> listMatterReportOrg() {
        if (SecurityContextHolder.getOrgId() != 0L) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(matterReportPlatService.listMatterReportOrg());
    }

    @PostMapping("/page")
    @ApiOperation("分页")
    @RequiresPermissions("Management:Classroom:Repairs")
    public R<IPage<MatterReportResponse>> pagePlatMatterReport(@RequestBody MatterReportPageRequest params) {
        if (SecurityContextHolder.getOrgId() != 0L) {
            return R.ok(params.createMpPage());
        }
        return R.ok(matterReportPlatService.pageMatterReport(params));
    }

    @PostMapping("detail")
    @ApiOperation("详情")
    @RequiresPermissions("Management:Classroom:Repairs")
    public R<MatterReportResponse> detailPlatMatterReport(@RequestBody @Validated IdRequest params) {
        if (SecurityContextHolder.getOrgId() != 0L) {
            return R.ok();
        }
        return R.ok(matterReportPlatService.manageDetail(params.getId()));
    }

}
