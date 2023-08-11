package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.enums.EnabledEnum;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.train.allowance.entity.*;
import cn.qbs.wa.train.allowance.enums.*;
import cn.qbs.wa.train.allowance.mapper.*;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyRequest;
import cn.qbs.wa.train.allowance.pojo.approve.*;
import cn.qbs.wa.train.allowance.pojo.workflowexecute.WorkflowEndRequest;
import cn.qbs.wa.train.allowance.pojo.workflowexecute.WorkflowExecuteRequest;
import cn.qbs.wa.train.allowance.pojo.workflowexecute.WorkflowResult;
import cn.qbs.wa.train.allowance.pojo.workflownode.WorkflowInstanceNodeResponse;
import cn.qbs.wa.train.allowance.service.*;
import cn.qbs.wa.train.basic.api.RemoteTrainUserService;
import cn.qbs.wa.train.basic.api.pojo.DTO.user.UserListDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.train.logistics.api.RemoteClassroomScheduleService;
import cn.qbs.wa.train.logistics.api.enums.UsePeriodEnum;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClassroomSchedule.ClassroomScheduleDTO;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("approveService")
public class ApproveServiceImpl implements ApproveService {

    @Resource
    private ApplyMapper applyMapper;

    @Resource
    private ApplySettleMapper applySettleMapper;

    @Resource
    private ApplySettleClassroomMapper applyClassroomMapper;

    @Resource
    private ApplySettleDormitoryMapper applyDormitoryMapper;

    @Resource
    private WorkflowExecuteService workflowExecuteService;

    @Resource
    private ApplyService applyService;

    @Resource
    private ApplySettleService applySettleService;

    @Resource
    private ApplySettleClassroomService applySettleClassroomService;

    @Resource
    private RemoteClassroomScheduleService remoteClassroomScheduleService;

    @Resource
    private RemoteTrainUserService remoteTrainUserService;

    @Resource
    private WorkflowInstanceNodeMapper workflowInstanceNodeMapper;

    @Resource
    private ApplyQualificationMapper applyQualificationMapper;

    @Resource
    private ApplyAllowanceMapper applyAllowanceMapper;

    @Resource
    private ApplyActivityMapper applyActivityMapper;


    @Override

    public IPage<ApplyPageResponse> pageSettleApply(ApplySettlePageRequest request) {
        IPage<ApplyPageResponse> responseIPage = applySettleMapper.page(request.createMpPage(), request);
        return this.pageApply(responseIPage);
    }

    private IPage<ApplyPageResponse> pageApply(IPage<ApplyPageResponse> responseIPage) {
        if (CollectionUtils.isEmpty(responseIPage.getRecords())) {
            return responseIPage;
        }

        List<Long> createByIds =
                responseIPage.getRecords().stream().map(ApplyPageResponse::getCreateBy).collect(Collectors.toList());
        Map<Long, String> map = selectCreateByName(createByIds);
        responseIPage.getRecords().forEach(l -> l.setApplyUserName(map.get(l.getCreateBy())));
        return responseIPage;
    }

    private Map<Long, String> selectCreateByName(List<Long> createByIds) {
        Map<Long, String> map = new HashMap<>();
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setIdList(createByIds);
        R<List<UserListResultDTO>> listR = remoteTrainUserService.listUser(userListDTO);
        if (listR.getCode() != Constants.SUCCESS) {
            throw new SecurityException("申请人信息查询出错");
        }
        if (CollectionUtils.isNotEmpty(listR.getData())) {
            map = listR.getData().stream().collect(Collectors.toMap(UserListResultDTO::getId,
                    UserListResultDTO::getRealName, (a, b) -> b));
        }
        return map;
    }

    @Override
    public ApplySettleDetailResponse detailSettleApply(Long applyId) {
        // 查询申请表单头信息
        ApplySettleDetailResponse response = applySettleMapper.selectByApplyId(applyId);
        // 查询申请教室信息
        List<ApplySettleClassroom> classrooms =
                applyClassroomMapper.selectList(
                        Wrappers.<ApplySettleClassroom>lambdaQuery()
                                .eq(ApplySettleClassroom::getApplySettleId, response.getId()));
        response.setClassroomList(BeanUtil.copyToList(classrooms, ApplyClassroomDTO.class));
        // 查询申请宿舍信息
        List<ApplySettleDormitory> dormitorys =
                applyDormitoryMapper.selectList(
                        Wrappers.<ApplySettleDormitory>lambdaQuery()
                                .eq(ApplySettleDormitory::getApplySettleId, response.getId()));
        response.setDormitoryList(BeanUtil.copyToList(dormitorys, ApplyDormitoryDTO.class));
        Map<Long, String> map = selectCreateByName(CollUtil.newArrayList(response.getCreateBy()));
        response.setApplyUser(map.get(response.getCreateBy()));
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Apply approveInvalidatedApply(ApproveRequest request) throws ServiceException {
        Apply apply = applyService.getById(request.getId());
        ApplySettle applySettle=applySettleService.lambdaQuery().eq(ApplySettle::getApplyId,request.getId()).one();
        if (Objects.isNull(apply) || Objects.isNull(applySettle) || !apply.getFlowStatus().equals(FlowStatusEnum.PASS.getCode())) {
            throw new ServiceException("当前申请表不存在或未通过");
        }
        apply.setApplyStatus(request.getApplyStatus());
        this.applyService.updateById(apply);
        if (apply.getApplyType().equals(ApplyTypeEnum.SETTLE.getCode())) {
            approveSettleInvalidatedApply(applySettle);
        }
        return apply;
    }

    public boolean approveSettleInvalidatedApply(ApplySettle applySettle) {

        // 获取申请单据的明细
        List<ApplySettleClassroom> applySettleClassrooms =
                applySettleClassroomService
                        .lambdaQuery()
                        .eq(ApplySettleClassroom::getApplySettleId, applySettle.getId())
                        .list();
        if(applySettleClassrooms!=null && !applySettleClassrooms.isEmpty()){
            remoteDeleteClassroomSchedule(applySettle.getApplyId());
        }

        return Boolean.TRUE;
    }

    private void  remoteDeleteClassroomSchedule(Long applyId) {
        R<Boolean> booleanR = remoteClassroomScheduleService.deleteByApplyId(applyId);
        if (booleanR.getCode() != Constants.SUCCESS) {
            throw new SecurityException("教室日程删除失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Apply approveApply(ApproveRequest request) throws ServiceException {
        Apply apply = applyService.getById(request.getApplyId());
        if (Objects.isNull(apply) || apply.getApplyResult() != null || !apply.getFlowStatus().equals(FlowStatusEnum.ING.getCode())) {
            throw new ServiceException("当前申请表不存在或已被审批完");
        }
        checkApplyManagerRefAndCurNodeId(apply, request.getCurNodeId());
        if (request.getResult().equals(ApplyResultEnum.NO_PASS.getCode())) {
            // 审批不通过
            applyNoPass(request, apply);
        } else {
            // 审批通过
            applyPass(apply, request);
        }
        return apply;
    }

    /**
     * 校验登录人是否有审批权限和当前节点是否已被审批
     *
     * @param apply
     * @param curNodeId
     * @throws ServiceException
     */
    private void checkApplyManagerRefAndCurNodeId(Apply apply, String curNodeId) throws ServiceException {
        WorkflowInstanceNodeResponse workflowInstanceNodeResponse =
                workflowInstanceNodeMapper.selectByApplyIdAndNode(apply.getId());
        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        Long userId = SecurityUtils.getUserId();
        roles.add(userId.toString());
        if (workflowInstanceNodeResponse == null || !roles.contains(workflowInstanceNodeResponse.getManagerRef())) {
            throw new ServiceException("当前单据已被审批完或你无权审批");
        }
        if (!workflowInstanceNodeResponse.getId().toString().equals(curNodeId)) {
            throw new ServiceException("当前节点已被审批");
        }
    }

    private void applyNoPass(ApproveRequest request, Apply apply) {

        WorkflowEndRequest workflowEndRequest = new WorkflowEndRequest();
        workflowEndRequest.setProcessNo(apply.getProcessNo());
        workflowEndRequest.setComment(request.getComment());
        workflowEndRequest.setAttachList(request.getAttachList());
        workflowEndRequest.setPass(false);
        workflowExecuteService.end(workflowEndRequest);
        apply.setApplyResult(ApplyResultEnum.NO_PASS.getCode());
        apply.setApprovalComment(request.getComment());
        apply.setFlowStatus(FlowStatusEnum.REJECT.getCode());
        this.applyService.updateById(apply);
    }

    private void applyPass(Apply apply, ApproveRequest request) {

        // 调用审批流程接口
        WorkflowExecuteRequest workflowExecuteRequest = new WorkflowEndRequest();
        workflowExecuteRequest.setAttachList(request.getAttachList());
        workflowExecuteRequest.setComment(request.getComment());
        workflowExecuteRequest.setProcessNo(apply.getProcessNo());
        WorkflowResult workflowResult = workflowExecuteService.next(workflowExecuteRequest);
        if (workflowResult.getEnded().equals(ApplyEndedEnum.NOT_OVER.getCode())) {
            // 如果还有下个流程待审批,单据状态不做处理
        } else {
            // 单据审批完成
            apply.setApplyResult(ApplyResultEnum.PASS.getCode());
            apply.setApprovalComment(request.getComment());
            apply.setFlowStatus(FlowStatusEnum.PASS.getCode());
            this.applyService.updateById(apply);

            if (apply.getApplyType().equals(ApplyTypeEnum.SETTLE.getCode())) {
                approveSettleApplyAfter(apply);
            }
        }
    }


    /**
     * 宿舍教室审批通过后处理
     *
     * @param apply
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveSettleApplyAfter(Apply apply) {

        // 获取申请单据的明细
        ApplySettle applySettle = applySettleService.lambdaQuery().eq(ApplySettle::getApplyId, apply.getId()).one();
        if (applySettle == null) {
            throw new ServiceException("申请单不存在");
        }
        List<ApplySettleClassroom> applySettleClassrooms =
                applySettleClassroomService
                        .lambdaQuery()
                        .eq(ApplySettleClassroom::getApplySettleId, applySettle.getId())
                        .list();
        if (CollUtil.isNotEmpty(applySettleClassrooms)) {
            // 构成每天日期排期表集合
            List<ClassroomScheduleDTO> applyRoomPeriods = applySettleClassrooms.stream().flatMap(e -> {
                // 获取教室使用起止时间的每一天
                List<LocalDate> days = this.listBetweenDays(e.getUseDateStart(), e.getUseDateEnd());
                return days.stream().map(d -> {
                    ClassroomScheduleDTO t = new ClassroomScheduleDTO();
                    t.setOrgId(applySettle.getOrgId());
                    t.setApplyId(applySettle.getApplyId());
                    t.setUseState(EnabledEnum.ENABLED.getValue());
                    t.setClassroomId(e.getClassroomId());
                    t.setUsePeriod(e.getUsePeriod());
                    t.setUseDate(d);
                    return t;
                });
            }).collect(Collectors.toList());

            // 批量远程插入教室日程
            R<Boolean> booleanR = remoteClassroomScheduleService.saveBatch(combineConflict(applyRoomPeriods));
            if (!booleanR.isOk()) {
                throw new ServiceException(booleanR.getMsg());
            }
        }
        return Boolean.TRUE;
    }

    /**
     * 整合冲突时间段
     * @param list
     * @return
     */
    private List<ClassroomScheduleDTO> combineConflict(List<ClassroomScheduleDTO> list) {
        List<ClassroomScheduleDTO> schedules = new ArrayList<>();
        for (ClassroomScheduleDTO schedule : list) {
            if (schedules.isEmpty()) {
                schedules.add(schedule);
            } else {
                // 同一间教室同一天时间段不冲突
                boolean noneMatch = schedules.stream().noneMatch(e ->
                        e.getClassroomId().equals(schedule.getClassroomId()) && e.getUseDate().equals(schedule.getUseDate())
                        && (e.getUsePeriod().equals(schedule.getUsePeriod()) || UsePeriodEnum.ALLDAY.getCode().equals(e.getUsePeriod()) || UsePeriodEnum.ALLDAY.getCode().equals(schedule.getUsePeriod())));
                if (noneMatch) {
                    schedules.add(schedule);
                }
            }
        }
        return schedules;
    }

    /**
     * 获取起止时间段每一天时间
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private List<LocalDate> listBetweenDays(LocalDate startDate, LocalDate endDate) {
        long numDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return Stream.iterate(startDate, date -> date.plusDays(1)).limit(numDays).collect(Collectors.toList());
    }

    @Override
    public IPage<ApplySettleClassroomPageResponse> pageSettleClassroomApply(ApplyPageRequest request) {
        return applyClassroomMapper.page(request.createMpPage(), request);
    }

    @Override
    public ApplySettleClassroomDetailResponse detailSettleClassroomApply(Long id) {
        return applyClassroomMapper.detail(id);
    }

    @Override
    public IPage<ApplyPageResponse> pageRecord(ApplyPageRequest request) {

        ApprovePageRequest approvePageRequest = BeanUtil.copyProperties(request, ApprovePageRequest.class);
        BeanUtil.copyProperties(request, approvePageRequest);
        Page<ApplyPageResponse> mpPage = request.createMpPage();
        //根据节点数据查询出所有单据
        IPage<ApplyPageResponse> applyPageResponseIPage = null;
        //TODO 课程、活动、资质申请记录
        /*if (request.getApplyType().equals(ApplyTypeEnum.ACTIVITY.getCode())) {
            applyPageResponseIPage = applyActivityMapper.page(mpPage, BeanUtil.copyProperties(request, ApplyRequest.class));
        } else if (request.getApplyType().equals(ApplyTypeEnum.ALLOWANCE.getCode())) {
            applyPageResponseIPage = applyAllowanceMapper.page(mpPage, BeanUtil.copyProperties(request, ApplyRequest.class));
        } else */if (request.getApplyType().equals(ApplyTypeEnum.SETTLE.getCode())) {
            applyPageResponseIPage = applySettleMapper.pageRecord(mpPage, BeanUtil.copyProperties(request, ApplySettlePageRequest.class));
        } /*else {
            applyPageResponseIPage = applyMapper.page(mpPage, BeanUtil.copyProperties(request, ApplyRequest.class));
        }*/
        if (applyPageResponseIPage == null || CollectionUtils.isEmpty(applyPageResponseIPage.getRecords())) {
            return applyPageResponseIPage;
        }
        applyPageResponseIPage = pageApply(applyPageResponseIPage);
        return applyPageResponseIPage;
    }

    @Override
    public IPage<ApplyPageResponse> pageV2(ApplyPageRequest request) {

        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        String userId = SecurityContextHolder.getUserId().toString();
        roles.add(userId);
        ApprovePageRequest approvePageRequest = BeanUtil.copyProperties(request, ApprovePageRequest.class);
        BeanUtil.copyProperties(request, approvePageRequest);
        approvePageRequest.setRoles(roles);
        //根据登录人查询出与之相关的节点数据
        List<WorkflowInstanceNode> workflowInstanceNodes =
                workflowInstanceNodeMapper.selectByParams(approvePageRequest);
        approvePageRequest.setApplyType(request.getApplyType());
        Page<ApplyPageResponse> mpPage = request.createMpPage();
        if (CollectionUtils.isEmpty(workflowInstanceNodes)) {
            return mpPage;
        }
        request.setProcessNos(workflowInstanceNodes.stream().map(WorkflowInstanceNode::getProcessNo).collect(Collectors.toList()));
        request.setFlag(1);
        //根据节点数据查询出所有单据
        IPage<ApplyPageResponse> applyPageResponseIPage = null;
        if (request.getApplyType().equals(ApplyTypeEnum.ACTIVITY.getCode())) {
            applyPageResponseIPage = applyActivityMapper.page(mpPage, BeanUtil.copyProperties(request, ApplyRequest.class));
        } else if (request.getApplyType().equals(ApplyTypeEnum.ALLOWANCE.getCode())) {
            applyPageResponseIPage = applyAllowanceMapper.page(mpPage, BeanUtil.copyProperties(request, ApplyRequest.class));
        } else if (request.getApplyType().equals(ApplyTypeEnum.SETTLE.getCode())) {
            applyPageResponseIPage = applySettleMapper.page(mpPage, BeanUtil.copyProperties(request, ApplySettlePageRequest.class));
        } else {
            applyPageResponseIPage = applyMapper.page(mpPage, BeanUtil.copyProperties(request, ApplyRequest.class));
        }
        if (applyPageResponseIPage == null || CollectionUtils.isEmpty(applyPageResponseIPage.getRecords())) {
            return applyPageResponseIPage;
        }
        applyPageResponseIPage = pageApply(applyPageResponseIPage);
        //对查询出的单据根据设置登录人的单据状态(优先审批中)
        applyPageResponseIPage.getRecords().forEach(l -> {
            workflowInstanceNodes.forEach(p -> {
                if (l.getProcessNo().equals(p.getProcessNo())) {
                    if (p.getStatus().equals(FlowNodeStatusEnum.IN_PROGRESS.getCode())) {
                        l.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getCode());
                    }
                    l.setCurNodeId(p.getId().toString());
                }
            });
        });
        applyPageResponseIPage.getRecords().stream().filter(l -> l.getStatus() == null).forEach(l -> l.setStatus(FlowNodeStatusEnum.PASS.getCode()));
        return applyPageResponseIPage;
    }

    @Override
    public ApproveByWorkflowNodeDTO selectApplyByWorkflowNode(IdRequest request) {
        ApproveByWorkflowNodeDTO approveByWorkflowNodeDTO = new ApproveByWorkflowNodeDTO();
        List<WorkflowInstanceNodeResponse> workflowInstanceNodeResponses =
                workflowInstanceNodeMapper.selectByApplyId(request.getId());
        if (CollectionUtils.isNotEmpty(workflowInstanceNodeResponses)) {
            workflowInstanceNodeResponses = applyService.selectApproverInfo(workflowInstanceNodeResponses);
            approveByWorkflowNodeDTO.setWorkflowInstanceNodeResponses(workflowInstanceNodeResponses);
        }
        return approveByWorkflowNodeDTO;
    }

    @Override
    public Long selectQualificationCount() {
        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        String userId = SecurityContextHolder.getUserId().toString();
        roles.add(userId);
        ApprovePageRequest approvePageRequest =new ApprovePageRequest();
        approvePageRequest.setRoles(roles);
        approvePageRequest.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getCode());
        //根据登录人查询出与之相关的节点数据
        List<WorkflowInstanceNode> workflowInstanceNodes =
                workflowInstanceNodeMapper.selectByParams(approvePageRequest);
        List<String> processNos=workflowInstanceNodes.stream().map(WorkflowInstanceNode::getProcessNo).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(processNos)){
            Long count=applyService.lambdaQuery().eq(Apply::getApplyType,ApplyTypeEnum.QUALIFICATION).in(Apply::getProcessNo,processNos).count();
            return count;
        }
        return 0L;
    }

    @Override
    public AllowanceAndActivityCount selectAllowanceAndActivityCount() {
        AllowanceAndActivityCount allowanceAndActivityCount=new AllowanceAndActivityCount();
        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        String userId = SecurityContextHolder.getUserId().toString();
        roles.add(userId);
        ApprovePageRequest approvePageRequest =new ApprovePageRequest();
        approvePageRequest.setRoles(roles);
        approvePageRequest.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getCode());
        //根据登录人查询出与之相关的节点数据
        List<WorkflowInstanceNode> workflowInstanceNodes =
                workflowInstanceNodeMapper.selectByParams(approvePageRequest);
        List<String> processNos=workflowInstanceNodes.stream().map(WorkflowInstanceNode::getProcessNo).collect(Collectors.toList());
        List<String> applyTypes=new ArrayList<>();
        applyTypes.add(ApplyTypeEnum.ALLOWANCE.getCode());
        applyTypes.add(ApplyTypeEnum.ACTIVITY.getCode());
        if(CollectionUtils.isNotEmpty(processNos)){
            Long count=applyService.lambdaQuery().in(Apply::getApplyType,applyTypes).in(Apply::getProcessNo,processNos).count();
            Long count2=applyService.lambdaQuery().in(Apply::getApplyType,ApplyTypeEnum.ALLOWANCE.getCode()).in(Apply::getProcessNo,processNos).count();
            Long count3=applyService.lambdaQuery().in(Apply::getApplyType,ApplyTypeEnum.ACTIVITY.getCode()).in(Apply::getProcessNo,processNos).count();
            allowanceAndActivityCount.setAllowanceAndActivityCount(count);
            allowanceAndActivityCount.setAllowanceCount(count2);
            allowanceAndActivityCount.setActivityCount(count3);
        }
        return allowanceAndActivityCount;
    }

    @Override
    public IPage<ApplyPageResponse> pageApproveApply(ApprovePageRequest request) {
        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        String userId = SecurityContextHolder.getUserId().toString();
        if (roles == null) {
            roles = CollUtil.newHashSet(userId);
        } else {
            roles.add(userId);
        }
        request.setRoles(roles);
        Page<ApplyPageResponse> page = request.createMpPage();
        long pageIdx = (page.getCurrent() - 1) * page.getSize();
        long pageSize = page.getSize();
        if (request.getApplyType().equals(ApplyTypeEnum.QUALIFICATION.getCode())) {
            if (request.getStatus() == null) {
                // 默认未审核的展示在前面
                long total = applyMapper.pageCount(request);
                if (total > 0) {
                    List <ApplyPageResponse> records = applyQualificationMapper.pageList(pageIdx, pageSize, request);
                    page.setRecords(records);
                }
                page.setTotal(total);
            } else {
                applyQualificationMapper.pageV2(page, request);
            }
        } else if (request.getApplyType().equals(ApplyTypeEnum.ACTIVITY.getCode())) {
            if (request.getStatus() == null) {
                // 默认未审核的展示在前面
                long total = applyActivityMapper.pageCount(request);
                if (total > 0) {
                    List <ApplyPageResponse> records = applyActivityMapper.pageList(pageIdx, pageSize, request);
                    page.setRecords(records);
                }
                page.setTotal(total);
            } else {
                applyActivityMapper.pageV2(page, request);
            }
        } else if (request.getApplyType().equals(ApplyTypeEnum.ALLOWANCE.getCode())) {
            if (request.getStatus() == null) {
                // 默认未审核的展示在前面
                long total = applyMapper.pageCount(request);
                if (total > 0) {
                    List <ApplyPageResponse> records = applyAllowanceMapper.pageList(pageIdx, pageSize, request);
                    page.setRecords(records);
                }
                page.setTotal(total);
            } else {
                applyAllowanceMapper.pageV2(page, request);
            }
        }
        return pageApply(page);
    }

    @Override
    public Long selectClassroomCount() {
        Set<String> roles = SecurityUtils.getLoginUser().getRoles();
        String userId = SecurityContextHolder.getUserId().toString();
        roles.add(userId);
        ApprovePageRequest approvePageRequest =new ApprovePageRequest();
        approvePageRequest.setRoles(roles);
        approvePageRequest.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getCode());
        //根据登录人查询出与之相关的节点数据
        List<WorkflowInstanceNode> workflowInstanceNodes =
                workflowInstanceNodeMapper.selectByParams(approvePageRequest);
        List<String> processNos=workflowInstanceNodes.stream().map(WorkflowInstanceNode::getProcessNo).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(processNos)){
            Long count=applyService.lambdaQuery().eq(Apply::getApplyType,ApplyTypeEnum.SETTLE).in(Apply::getProcessNo,processNos).count();
            return count;
        }
        return 0L;
    }

}
