package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.train.allowance.entity.Apply;
import cn.qbs.wa.train.allowance.entity.ApplySettle;
import cn.qbs.wa.train.allowance.entity.ApplySettleClassroom;
import cn.qbs.wa.train.allowance.entity.ApplySettleDormitory;
import cn.qbs.wa.train.allowance.enums.*;
import cn.qbs.wa.train.allowance.mapper.ApplyAttachMapper;
import cn.qbs.wa.train.allowance.mapper.ApplyMapper;
import cn.qbs.wa.train.allowance.mapper.ApplySettleMapper;
import cn.qbs.wa.train.allowance.pojo.apply.ApplySettleClassroomUpdateRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplySettleDormitoryUpdateRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyUpdateBySettleRequest;
import cn.qbs.wa.train.allowance.service.ApplySettleClassroomService;
import cn.qbs.wa.train.allowance.service.ApplySettleDormitoryService;
import cn.qbs.wa.train.allowance.service.ApplySettleService;
import cn.qbs.wa.train.logistics.api.RemoteClassroomScheduleService;
import cn.qbs.wa.train.logistics.api.enums.UsePeriodEnum;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClassroomSchedule.ClassroomScheduleDTO;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 入驻申请(ApplySettle)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
@Slf4j
@Service("applySettleService")
public class ApplySettleServiceImpl extends ServiceImpl<ApplySettleMapper, ApplySettle> implements ApplySettleService {

    @Resource
    private ApplyMapper applyMapper;

    @Resource
    private ApplySettleClassroomService applySettleClassroomService;

    @Resource
    private ApplySettleDormitoryService applySettleDormitoryService;

    @Resource
    private ApplyAttachMapper applyAttachMapper;

    @Resource
    private RemoteClassroomScheduleService remoteClassroomScheduleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean copySettleApply(Long applyId) {
        Apply apply = applyMapper.selectById(applyId);
        if (apply == null) {
            throw new ServiceException("申请不存在，请刷新后操作");
        }
        Long orgId = SecurityContextHolder.getOrgId();
        if (!orgId.equals(apply.getOrgId())) {
            throw new ServiceException("非法操作");
        }
        if (ApplyResultEnum.NO_PASS.getCode().equals(apply.getApplyResult()) || ApplyStatusEnum.INVALIDATED.getCode().equals(apply.getApplyStatus())) {

        }else {
            throw new ServiceException("复制功能，仅只支持驳回或者作废的申请");
        }
        ApplySettle  settle= this.lambdaQuery().eq(ApplySettle::getApplyId, applyId).one();
        if (settle == null) {
            throw new ServiceException("申请不存在，请刷新后操作");
        }
        Long sourceSettleId = settle.getId();
        // 保存表头信息
        // 清空旧ID值
        apply.setId(null);
        apply.setFlowStatus(null);
        apply.setApplyResult(null);
        apply.setProcessNo(null);
        apply.setApplyStatus(ApplyStatusEnum.NOT_SUBMIT.getCode());
        applyMapper.insert(apply);
        // 保存资助申请表单信息
        Long targetApplyId = apply.getId();
        settle.setApplyId(targetApplyId);
        settle.setId(null);
        boolean save = this.save(settle);

        // 拷贝成新增对象
        Long targetSettleId = settle.getId();

        applySettleClassroomService.copyByApplyId(sourceSettleId, targetSettleId);

        applySettleDormitoryService.copyByApplyId(sourceSettleId, targetSettleId);

        // 拷贝附件
        applyAttachMapper.copyByApplyId(applyId, AttachSectionEnum.APPLY.getCode(), targetApplyId);

        return save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(ApplyUpdateBySettleRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        //更改表单数据
        Apply apply = BeanUtil.copyProperties(params, Apply.class);
        if (StringUtils.isNotBlank(apply.getContactNumber())) {
            apply.setContactNumber(AesUtil.encode(apply.getContactNumber()));
        }
        if (StringUtils.isNotBlank(apply.getLegalNumber())) {
            apply.setLegalNumber(AesUtil.encode(apply.getLegalNumber()));
        }
        apply.setId(params.getApplyId());
        applyMapper.updateById(apply);

        //更改机构入驻数据
        ApplySettle applySettle = BeanUtil.copyProperties(params, ApplySettle.class);

        //将教室申请集合先删除再插入
        List<ApplySettleClassroom> settleClassrooms =
                applySettleClassroomService.lambdaQuery().eq(ApplySettleClassroom::getApplySettleId,
                        applySettle.getId()).list();
        if (CollectionUtils.isNotEmpty(settleClassrooms)) {
            applySettleClassroomService.removeByIds(settleClassrooms);
        }
        List<ApplySettleClassroomUpdateRequest> applySettleClassroomUpdateRequests =
                params.getApplySettleClassroomUpdateRequests();
        if (CollectionUtils.isNotEmpty(applySettleClassroomUpdateRequests)) {
            List<ApplySettleClassroom> applySettleClassrooms = TreeUtil.copyBeanList(applySettleClassroomUpdateRequests,
                    ApplySettleClassroom.class);
            applySettleClassroomService.saveBatch(applySettleClassrooms);
        }

        //将宿舍申请集合先删除再插入
        List<ApplySettleDormitory> settleDormitorys =
                applySettleDormitoryService.lambdaQuery().eq(ApplySettleDormitory::getApplySettleId,
                        applySettle.getId()).list();
        if (CollectionUtils.isNotEmpty(settleDormitorys)) {
            applySettleDormitoryService.removeByIds(settleDormitorys);
        }
        List<ApplySettleDormitoryUpdateRequest> applySettleDormitoryUpdateRequests =
                params.getApplySettleDormitoryUpdateRequests();
        if (CollectionUtils.isNotEmpty(applySettleDormitoryUpdateRequests)) {
            List<ApplySettleDormitory> applySettleDormitorys = TreeUtil.copyBeanList(applySettleDormitoryUpdateRequests,
                    ApplySettleDormitory.class);
            applySettleDormitoryService.saveBatch(applySettleDormitorys);
        }

        return this.updateById(applySettle);
    }

    @Override
    public void checkCompleteness(Long applyId) {
        ApplySettle one = this.lambdaQuery().eq(ApplySettle::getApplyId, applyId).one();
        if (one == null) {
            throw new ServiceException("该培训申请信息不完整");
        }
        if (!one.getOrgId().equals(SecurityContextHolder.getOrgId())) {
            throw new ServiceException("非法操作");
        }

        Long applySettleId = one.getId();

        // 将教室申请数据
        Long count = applySettleClassroomService.lambdaQuery().eq(ApplySettleClassroom::getApplySettleId, applySettleId).count();
        if (count == null || count == 0) {
            throw new ServiceException("该培训申请中, 缺少教室数据，请补充完善");
        }

        // 将宿舍申请数据
        // count = applySettleDormitoryService.lambdaQuery().eq(ApplySettleDormitory::getApplySettleId, applySettleId).count();
        // if (count == null || count == 0) {
        //     throw new ServiceException("该培训申请中, 缺少宿舍数据，请补充完善");
        // }

    }

    @Override
    public void checkSettleOccupy(Long applyId) {

        // 获取申请单据的明细
        ApplySettle applySettle = this.lambdaQuery().eq(ApplySettle::getApplyId, applyId).one();
        if (applySettle == null) {
            throw new ServiceException("申请单号不存在");
        }
        List<ApplySettleClassroom> applySettleClassrooms =
                applySettleClassroomService
                        .lambdaQuery()
                        .eq(ApplySettleClassroom::getApplySettleId, applySettle.getId())
                        .list();
        // 当前申请单中每间教室对应的申请日期集合
        Map<Long, Set<ClassroomScheduleDTO>> roomToDays = new HashMap<>();
        for (ApplySettleClassroom applySettleClassroom : applySettleClassrooms) {
            // 获取起止时间段的每一天集合
            Set<LocalDate> daySet = listBetweenDays(applySettleClassroom.getUseDateStart(), applySettleClassroom.getUseDateEnd());
            if (!daySet.isEmpty()) {
                Long classroomId = applySettleClassroom.getClassroomId();
                String usePeriod = applySettleClassroom.getUsePeriod();
                // 构造每一条申请记录对应的教室时间表
                Set<ClassroomScheduleDTO> roomDaySet = daySet.stream().map(d -> {
                    ClassroomScheduleDTO classroomScheduleDTO = new ClassroomScheduleDTO();
                    classroomScheduleDTO.setClassroomId(classroomId);
                    classroomScheduleDTO.setUseDate(d);
                    classroomScheduleDTO.setUsePeriod(usePeriod);
                    return classroomScheduleDTO;
                }).collect(Collectors.toSet());

                if (roomToDays.containsKey(classroomId)) {
                    // 判断是否存在时间冲突
                    Set<ClassroomScheduleDTO> finalDaySet = roomToDays.get(classroomId);
                    Optional.ofNullable(hasIntersection(roomDaySet, finalDaySet)).ifPresent(r -> {
                        throw new ServiceException(StrUtil.format("申请单中教室{}在{}日存在冲突", applySettleClassroom.getRoomNo(), r.getUseDate()));
                    });
                    finalDaySet.addAll(roomDaySet);
                } else {
                    roomToDays.put(classroomId, roomDaySet);
                }
            }
        }
        // 构成每天日期集合
        Set<ClassroomScheduleDTO> classroomScheduleDTOS = roomToDays.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        checkClassroomUse(applyId, applySettleClassrooms, classroomScheduleDTOS);

    }

    private void checkClassroomUse(Long applyId, List<ApplySettleClassroom> applySettleClassrooms,
                                   Set<ClassroomScheduleDTO> classroomScheduleDTOS) {
        if (CollUtil.isEmpty(classroomScheduleDTOS)) {
            return;
        }
        Set<Long> classroomIds = classroomScheduleDTOS.stream().map(ClassroomScheduleDTO::getClassroomId).collect(Collectors.toSet());

        // 查询"审批中"教室时间段是否与当前申请教室时间段冲突
        List<Apply> applyList = this.applyMapper.selectList(
                Wrappers.<Apply>lambdaQuery().select(Apply::getId)
                        .eq(Apply::getApplyType, ApplyTypeEnum.SETTLE.getCode())
                        .eq(Apply::getFlowStatus, FlowStatusEnum.ING.getCode())
        );
        List<Long> applyIds = applyList.stream().map(Apply::getId).filter(e -> !e.equals(applyId)).collect(Collectors.toList());
        // 根据申请ID以及班级ID，查询相应的申请记录
        List<ApplySettleClassroom> occupyRecords = this.baseMapper.listClassroomSchedule(applyIds, classroomIds);

        if (CollUtil.isNotEmpty(occupyRecords)) {
            // 构成每天日期集合
            Set<ClassroomScheduleDTO> occupyRoomPeriods = occupyRecords.stream().flatMap(e -> {
                Set<LocalDate> days = this.listBetweenDays(e.getUseDateStart(), e.getUseDateEnd());
                return days.stream().map(d -> {
                    ClassroomScheduleDTO t = new ClassroomScheduleDTO();
                    t.setClassroomId(e.getClassroomId());
                    t.setUsePeriod(e.getUsePeriod());
                    t.setUseDate(d);
                    return t;
                });
            }).collect(Collectors.toSet());

            Optional.ofNullable(hasIntersection(classroomScheduleDTOS, occupyRoomPeriods)).ifPresent(r -> {
                Map<Long, String> dictMap = applySettleClassrooms.stream().collect(Collectors.toMap(ApplySettleClassroom::getClassroomId, ApplySettleClassroom::getRoomNo, (a, b) -> a));
                throw new ServiceException(StrUtil.format("教室{}在{}日{}已在申请审批中，请调整申请时间", dictMap.get(r.getClassroomId()), r.getUseDate(), UsePeriodEnum.getNameByCode(r.getUsePeriod())));
            });
        }

        // 校验"审批通过"教室时间段是否与当前申请教室时间段冲突
        R<Boolean> booleanR = remoteClassroomScheduleService.checkClassroomUse(new ArrayList<>(classroomScheduleDTOS));
        if (!booleanR.isOk()) {
            throw new ServiceException(booleanR.getMsg());
        }
    }

    /**
     * 获取起止时间段每一天时间
     */
    private Set<LocalDate> listBetweenDays(LocalDate startDate, LocalDate endDate) {
        long numDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return Stream.iterate(startDate, date -> date.plusDays(1)).limit(numDays).collect(Collectors.toSet());
    }

    /**
     * 判断集合是否存在交集
     */
    public ClassroomScheduleDTO hasIntersection(Set<ClassroomScheduleDTO> set1, Set<ClassroomScheduleDTO> set2) {
        for (ClassroomScheduleDTO s1 : set1) {
            if (set2.contains(s1)) {
                return s1;
            }
            for (ClassroomScheduleDTO s2 : set2) {
                if (s2.getClassroomId().equals(s1.getClassroomId()) && s2.getUseDate().equals(s1.getUseDate())) {
                    // 全天 与 上午、下午 互斥
                    if (UsePeriodEnum.ALLDAY.getCode().equals(s2.getUsePeriod())
                            || UsePeriodEnum.ALLDAY.getCode().equals(s1.getUsePeriod())) {
                        return s2;
                    }
                }
            }

        }
        return null;
    }
}

