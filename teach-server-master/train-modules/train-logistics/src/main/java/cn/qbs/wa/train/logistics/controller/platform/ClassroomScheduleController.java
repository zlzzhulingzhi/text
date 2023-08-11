package cn.qbs.wa.train.logistics.controller.platform;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.train.logistics.api.enums.UsePeriodEnum;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.ClassroomSchedule;
import cn.qbs.wa.train.logistics.mapper.ClassroomMapper;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.*;
import cn.qbs.wa.train.logistics.service.ClassroomScheduleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 教室日程表(ClassroomSchedule)表控制层
 *
 * @author makejava
 * @since 2022-10-18 11:23:14
 */
@Slf4j
@RestController
@RequestMapping("classroomSchedule")
public class ClassroomScheduleController {

    /**
     * 服务对象
     */
    @Resource
    private ClassroomScheduleService classroomScheduleService;

    @Resource
    private ClassroomMapper classroomMapper;

    /**
     * 新增教室日程表
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    // @RequiresPermissions("classroomSchedule:add")
    // @Log(title = "新增教室日程表", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated ClassroomScheduleAddRequest params) {
        return R.ok(this.classroomScheduleService.add(params));
    }

    /**
     * 分页查询教室日程表
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    // @RequiresPermissions("classroomSchedule:page")
    // @Log(title = "分页查询教室日程表", businessType = BusinessType.OTHER)
    public R<IPage<ClassroomSchedulePageResponse>> page(@RequestBody ClassroomSchedulePageRequest params) {
        return R.ok(this.classroomScheduleService.page(params));
    }

    /**
     * 查询教室日程表详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    // @RequiresPermissions("classroomSchedule:details")
    // @Log(title = "教室日程表详情", businessType = BusinessType.OTHER)
    public R<ClassroomScheduleDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.classroomScheduleService.detail(request.getId()));
    }

    /**
     * 修改教室日程表
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    // @RequiresPermissions("classroomSchedule:update")
    // @Log(title = "更新教室日程表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ClassroomScheduleUpdateRequest params) {
        return R.ok(this.classroomScheduleService.update(params));
    }

    /**
     * 删除教室日程表
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    // @RequiresPermissions("classroomSchedule:delete")
    // @Log(title = "删除教室日程表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.classroomScheduleService.deleteByIds(request.getIdList()));
    }

    /**
     * 批量插入教室日程表
     *
     * @return
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("saveBatch")
    @ApiOperation("批量新增")
    public R<Boolean> saveBatch(@RequestBody List<ClassroomScheduleAddRequest> request) {
        Set<ClassroomSchedule> classroomSchedules = new HashSet<>();
        request.forEach(l -> {
            checkClassroomSchedule(l);
            ClassroomSchedule classroomSchedule = new ClassroomSchedule();
            BeanUtils.copyProperties(l, classroomSchedule);
            classroomSchedules.add(classroomSchedule);
        });
        boolean saveBatch;
        try {
            saveBatch = this.classroomScheduleService.saveBatch(classroomSchedules);
        } catch (DuplicateKeyException e) {
            log.error("数据库唯一键冲突", e);
            throw new ServiceException("当前申请的教室时间段存在被占用情况，请核实！");
        }
        return R.ok(saveBatch);
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("checkClassroomUse")
    @ApiOperation("确认教室使用情况")
    public R<Boolean> checkClassroomUse(@RequestBody List<ClassroomScheduleAddRequest> request) {
        request.forEach(this::checkClassroomSchedule);
        return R.ok(true);
    }

    private void checkClassroomSchedule(ClassroomScheduleAddRequest l) {
        LambdaQueryWrapper<ClassroomSchedule> wrapper = Wrappers.<ClassroomSchedule>lambdaQuery()
                .eq(ClassroomSchedule::getClassroomId, l.getClassroomId())
                .eq(ClassroomSchedule::getUseDate, l.getUseDate());
        if (!UsePeriodEnum.ALLDAY.getCode().equals(l.getUsePeriod())) {
            wrapper.in(StrUtil.isNotBlank(l.getUsePeriod()), ClassroomSchedule::getUsePeriod, UsePeriodEnum.ALLDAY.getCode(), l.getUsePeriod());
        }
        long count = classroomScheduleService.count(wrapper);
        if (count > 0) {
            Classroom classroom = classroomMapper.selectById(l.getClassroomId());
            throw new ServiceException(StrUtil.format("教室{}在{}日{}已被占用，请核实！", classroom.getRoomNo(), l.getUseDate(), UsePeriodEnum.getNameByCode(l.getUsePeriod())));
        }
    }

    @PostMapping("deleteByApplyId")
    @ApiOperation("删除教室日程表")
    public R<Boolean> deleteByApplyId(@RequestBody Long applyId) {
        List<ClassroomSchedule> classroomSchedules = new ArrayList<>();
        List<ClassroomSchedule> classroomScheduleList = classroomScheduleService.lambdaQuery().
                eq(ClassroomSchedule::getApplyId, applyId).list();
        classroomSchedules.addAll(classroomScheduleList);
        List<Long> ids = classroomSchedules.stream().map(ClassroomSchedule::getId).collect(Collectors.toList());
        return R.ok(this.classroomScheduleService.deleteByIds(ids));
    }
}
