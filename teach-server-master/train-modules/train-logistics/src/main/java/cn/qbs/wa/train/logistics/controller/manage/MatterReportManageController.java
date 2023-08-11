package cn.qbs.wa.train.logistics.controller.manage;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.pojo.matter.*;
import cn.qbs.wa.train.logistics.service.manage.MatterReportManageService;
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
 * @author yjx
 */
@RestController
@RequestMapping("/manage")
@Api(tags = "报事报修")
public class MatterReportManageController {

    @Resource
    private MatterReportManageService matterReportManageService;

    @PostMapping("detail")
    @ApiOperation("机构报事报修详情")
    public R<MatterReportResponse> detailOrgMatterReport(@RequestBody @Validated IdRequest params) {
        MatterReportResponse detail = matterReportManageService.manageDetail(params.getId());
        if (!detail.getOrgId().equals(SecurityContextHolder.getOrgId())) {
            return R.ok(new MatterReportResponse());
        }
        return R.ok(detail);
    }

    @PostMapping("page")
    @ApiOperation("机构报事报修分页")
    public R<IPage<MatterReportResponse>> pageOrgMatterReport(@RequestBody MatterReportPageRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(matterReportManageService.pageMatterReport(params));
    }

    @PostMapping("/matter-report/classroom/list")
    @ApiOperation("报事报修-班级列表")
    public R<List<Classroom>> listMatterReportClassroom() {
        Long orgId = SecurityContextHolder.getOrgId();
        if (orgId == 0L) {
            orgId = null;
        }
        return R.ok(matterReportManageService.listMatterReportClassroom(orgId));
    }

}
