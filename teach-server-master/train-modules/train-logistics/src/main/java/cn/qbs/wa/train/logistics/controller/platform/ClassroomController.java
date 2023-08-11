package cn.qbs.wa.train.logistics.controller.platform;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.ClassroomSchedule;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.excel.ClassroomDataParseResult;
import cn.qbs.wa.train.logistics.pojo.classroom.*;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.ClassroomScheduleDetailResponse;
import cn.qbs.wa.train.logistics.service.ClassroomScheduleService;
import cn.qbs.wa.train.logistics.service.OrganizationService;
import cn.qbs.wa.train.logistics.service.platform.ClassroomService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 教室表(Classroom)表控制层
 *
 * @author makejava
 * @since 2022-10-11 17:30:11
 */
@RestController
@RequestMapping("classroom")
@Api(tags = "教室管理")
public class ClassroomController {

    /**
     * 服务对象
     */
    @Resource
    private ClassroomService classroomService;

    @Resource
    ClassroomScheduleService classroomScheduleService;

    @Resource
    OrganizationService organizationService;

    /**
     * 新增教室表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @ApiOperation("新增教室")
    @RequiresPermissions("Management:Classroom")
    // @Log(title = "新增教室表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated ClassroomAddRequest params) {
        return R.ok(this.classroomService.add(params));
    }

    /**
     * 分页查询教室表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @ApiOperation("分页查询教室")
    @RequiresPermissions("Management:Classroom")
    // @Log(title = "分页查询教室表", businessType = BusinessType.OTHER)
    public R<IPage<ClassroomPageResponse>> page(@RequestBody ClassroomPageRequest params) {
        return R.ok(this.classroomService.page(params));
    }

    /**
     * 分页查询教室表
     *
     * @param params
     * @return
     */
    @PostMapping("classroomSchedulePage")
    @ApiOperation("分页查询教室日程")
    // @RequiresPermissions("classroom:page")
    // @Log(title = "分页查询教室表", businessType = BusinessType.OTHER)
    public R<IPage<ClassroomPageResponse>> classroomSchedulePage(
            @RequestBody ClassroomPageRequest params) {
        return R.ok(this.classroomService.classroomSchedulePage(params));
    }

    /**
     * 查询教室表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("教室详情")
    @RequiresPermissions("Management:Classroom")
    // @Log(title = "教室表详情", businessType = BusinessType.OTHER)
    public R<ClassroomDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.classroomService.detail(request.getId()));
    }

    /**
     * 修改教室表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @ApiOperation("修改教室")
    @RequiresPermissions("Management:Classroom")
    // @Log(title = "更新教室表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ClassroomUpdateRequest params) {
        return R.ok(this.classroomService.update(params));
    }

    /**
     * 批量修改教室表
     *
     * @param params
     * @return
     */
    @PostMapping("updateBatch")
    @ApiOperation("批量修改教室")
    // @RequiresPermissions("classroom:update")
    // @Log(title = "更新教室表", businessType = BusinessType.UPDATE)
    public R<Boolean> updateBatch(@RequestBody @Validated ClassroomUpdateBatchRequest params) {
        return R.ok(this.classroomService.updateBatch(params));
    }

    /**
     * 删除教室表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @ApiOperation("删除教室")
    @RequiresPermissions("Management:Classroom")
    // @Log(title = "删除教室表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.classroomService.deleteByIds(request.getIdList()));
    }

    @PostMapping("list")
    @ApiOperation("查询教室列表")
    public R<List<Classroom>> listClassroom(@RequestBody ClassroomPageRequest request) {
        return R.ok(this.classroomService.listClassroom(request));
    }

    @PostMapping("getClassroomState")
    @ApiOperation("教室预订分布")
    @RequiresPermissions("Management:Classroom:Schedule")
    public R<IPage<ClassroomPageResponse>> getClassroomState(@RequestBody ClassroomPageRequest params) {
        IPage<ClassroomPageResponse> p;
        if (params.getUseState() != null) {
            if (params.getUseState().equals(Constants.DB_TRUE)) {
                // 查询被占用的教室
                p = classroomService.pageInuse(params);
                for (ClassroomPageResponse record : p.getRecords()) {
                    // 依次查询教室在时间范围内被预定的记录数据
                    List<ClassroomSchedule> scheduleList = classroomScheduleService.lambdaQuery()
                            .select(ClassroomSchedule::getUseDate, ClassroomSchedule::getUseState, ClassroomSchedule::getUsePeriod, ClassroomSchedule::getOrgId)
                            .eq(ClassroomSchedule::getClassroomId, record.getId())
                            .between(ClassroomSchedule::getUseDate, params.getStartDate(), params.getEndDate())
                            .orderByAsc(ClassroomSchedule::getUseDate)
                            .list();
                    record.setClassroomScheduleDetailResponses(BeanUtil.copyToList(scheduleList, ClassroomScheduleDetailResponse.class));
                    if(CollectionUtils.isNotEmpty(record.getClassroomScheduleDetailResponses())){
                        for (ClassroomScheduleDetailResponse classroomScheduleDetailResponse : record.getClassroomScheduleDetailResponses()) {
                            Organization organization = organizationService.getById(classroomScheduleDetailResponse.getOrgId());
                            classroomScheduleDetailResponse.setOrgName(organization.getName());
                        }
                    }
                }
            } else {
                // 查询未被占用的教室
                p = classroomService.pageUnused(params);
            }
        } else {
            p = this.classroomService.getClassroomState(params);
        }
        return R.ok(p);
    }

    @PostMapping("detailFee")
    @ApiOperation("教室收费详情")
    @RequiresPermissions("Management:Classroom")
    public R<ClassroomPriceResponse> detailFee(@RequestBody IdRequest request) {
        return R.ok(this.classroomService.detailFee(request.getId()));
    }

    @ApiOperation(value = "预导入教室信息")
    @PostMapping("preview")
    @RequiresPermissions("Management:Classroom")
    public R importPre(MultipartFile file) {
        return R.ok(classroomService.importPre(file));
    }

    @PostMapping("batchAdd")
    @ApiOperation("批量新增教室")
    @RequiresPermissions("Management:Classroom")
    // @Log(title = "新增教室表", businessType = BusinessType.INSERT)
    public R<List<Classroom>> batchAdd(@RequestBody @Validated List<ClassroomDataParseResult> params) {
        return R.ok(this.classroomService.batchAdd(params));
    }

    @PostMapping("getClassroomSummary")
    @ApiOperation("教室预订统计")
    public R<IPage<ClassroomSummaryResponse>> getClassroomSummary(@RequestBody ClassroomSummaryRequest params) {
        return R.ok(this.classroomService.getClassroomSummary(params));
    }

    @PostMapping("getClassroomSummaryDetail")
    @ApiOperation("教室预订统计详情")
    public R<IPage<ClassroomPageResponse>> getClassroomSummaryDetail(@RequestBody ClassroomSummaryRequest params) {
        return R.ok(this.classroomService.getClassroomSummaryDetail(params));
    }

    @PostMapping("getClassroomSummaryDetailV2")
    @ApiOperation("教室预订统计详情（增加时间段）")
    public R<IPage<ClassroomPageResponse>> getClassroomSummaryDetailV2(@RequestBody ClassroomSummaryRequest params) {
        return R.ok(this.classroomService.getClassroomSummaryDetailV2(params));
    }

}
