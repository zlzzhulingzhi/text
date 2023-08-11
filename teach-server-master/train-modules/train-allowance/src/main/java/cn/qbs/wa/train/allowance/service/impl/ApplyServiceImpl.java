package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.enums.EnabledEnum;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.train.allowance.config.RedisCacheKey;
import cn.qbs.wa.train.allowance.entity.Apply;
import cn.qbs.wa.train.allowance.entity.ApplySettle;
import cn.qbs.wa.train.allowance.entity.ApplySettleClassroom;
import cn.qbs.wa.train.allowance.entity.ApplySettleDormitory;
import cn.qbs.wa.train.allowance.enums.*;
import cn.qbs.wa.train.allowance.mapper.ApplyMapper;
import cn.qbs.wa.train.allowance.mapper.ApplySettleMapper;
import cn.qbs.wa.train.allowance.mapper.WorkflowInstanceNodeMapper;
import cn.qbs.wa.train.allowance.pojo.apply.*;
import cn.qbs.wa.train.allowance.pojo.approve.ApplySettlePageRequest;
import cn.qbs.wa.train.allowance.pojo.workflowexecute.WorkflowStartResult;
import cn.qbs.wa.train.allowance.pojo.workflownode.WorkflowInstanceNodeResponse;
import cn.qbs.wa.train.allowance.service.*;
import cn.qbs.wa.train.basic.api.RemoteTrainRoleService;
import cn.qbs.wa.train.basic.api.RemoteTrainUserService;
import cn.qbs.wa.train.basic.api.pojo.DTO.role.RoleDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.role.RoleRequestDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.user.UserListDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.train.logistics.api.RemoteClassroomScheduleService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClassroomSchedule.ClassroomScheduleDTO;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 申请表(Apply)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
@Slf4j
@Service("applyService")
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {

    @Resource
    private ApplySettleService applySettleService;

    @Resource
    private ApplySettleClassroomService applySettleClassroomService;

    @Resource
    private ApplySettleDormitoryService applySettleDormitoryService;

    @Resource
    private WorkflowExecuteService workflowExecuteService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ApplySettleMapper applySettleMapper;

    @Resource
    private WorkflowInstanceNodeMapper workflowInstanceNodeMapper;

    @Resource
    private RemoteTrainRoleService remoteTrainRoleService;

    @Resource
    private RemoteTrainUserService remoteTrainUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long settleAdd(ApplyAddRequest request) {

        // 新增申请
        Apply apply = new Apply();
        BeanUtil.copyProperties(request, apply);
        apply.setFlowCode(FlowCodeEnum.ORG_SETTLE.getCode());
        if (StringUtils.isNotBlank(apply.getContactNumber())) {
            apply.setContactNumber(AesUtil.encode(apply.getContactNumber()));
        }
        this.save(apply);

        // 新增入驻申请明细
        ApplySettleAddRequest applySettleAddRequest = request.getApplySettleAddRequest();
        ApplySettle applySettleAddDTO = new ApplySettle();
        BeanUtil.copyProperties(applySettleAddRequest, applySettleAddDTO);
        applySettleAddDTO.setApplyId(apply.getId());
        applySettleService.save(applySettleAddDTO);

        // 新增入驻申请明细-教室
        List<ApplySettleClassroomAddRequest> applySettleClassroomAddRequests =
                request.getApplySettleAddRequest().getApplySettleClassroomAddRequests();
        if (CollectionUtils.isNotEmpty(applySettleClassroomAddRequests)) {
            List<ApplySettleClassroom> applySettleClassrooms = new ArrayList<>();
            applySettleClassroomAddRequests.forEach(
                    l -> {
                        ApplySettleClassroom applySettleClassroom = new ApplySettleClassroom();
                        BeanUtil.copyProperties(l, applySettleClassroom);
                        applySettleClassroom.setApplySettleId(applySettleAddDTO.getId());
                        applySettleClassrooms.add(applySettleClassroom);
                    });
            this.applySettleClassroomService.saveBatch(applySettleClassrooms);
        }

        // 新增入驻申请明细-宿舍
        List<ApplySettleDormitoryAddRequest> applySettleDormitoryAddRequests =
                request.getApplySettleAddRequest().getApplySettleDormitoryAddRequests();
        if (CollectionUtils.isNotEmpty(applySettleDormitoryAddRequests)) {
            List<ApplySettleDormitory> applySettleDormitories = new ArrayList<>();
            applySettleDormitoryAddRequests.forEach(
                    l -> {
                        ApplySettleDormitory applySettleDormitory = new ApplySettleDormitory();
                        BeanUtil.copyProperties(l, applySettleDormitory);
                        applySettleDormitory.setApplySettleId(applySettleAddDTO.getId());
                        applySettleDormitories.add(applySettleDormitory);
                    });
            this.applySettleDormitoryService.saveBatch(applySettleDormitories);
        }

        return apply.getId();
    }

    @Override
    public IPage<ApplyPageResponse> page(IPage<?> page, ApplyRequest applyRequest) {
        return this.baseMapper.pageCommonly(page, applyRequest);
    }


    @Override
    public IPage<ApplyPageResponse> pageSettleApply(ApplySettlePageRequest request) {
        request.setApplyType(ApplyTypeEnum.SETTLE.getCode());
        request.setOrgId(SecurityContextHolder.getOrgId());
        return this.applySettleMapper.page(request.createMpPage(), request);
    }

    @Override
    public void checkApplyStatus(Long applyId, boolean isDel) {
        // 查询表单信息
        Apply apply = this.getById(applyId);
        if (apply == null) {
            throw new ServiceException("申请表单不存在");
        }
        if (!apply.getOrgId().equals(SecurityContextHolder.getOrgId())) {
            throw new ServiceException("非法操作");
        }
        // 检查当前表单状态
        if (!isDel && ApplyStatusEnum.SUBMIT.getCode().equals(apply.getApplyStatus())) {
            throw new ServiceException("当前申请已提交，无法变更");
        }
        // 已提交 未驳回的无法删除
        if (isDel && ApplyStatusEnum.SUBMIT.getCode().equals(apply.getApplyStatus())
                && !ApplyResultEnum.NO_PASS.getCode().equals(apply.getApplyResult())) {
            throw new ServiceException("当前申请无法删除");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean submit(Long applyId) {
        Boolean flag = Boolean.FALSE;
        RLock lock = redissonClient.getLock(RedisCacheKey.getLockKey(applyId));
        if (lock.tryLock()) {
            try {
                // 查询表单信息
                Apply apply = this.getById(applyId);
                if (apply == null) {
                    throw new ServiceException("申请表单不存在");
                }
                if (!apply.getOrgId().equals(SecurityContextHolder.getOrgId())) {
                    throw new ServiceException("非法操作");
                }
                // 检查当前表单状态
                if (ApplyStatusEnum.SUBMIT.getCode().equals(apply.getApplyStatus())) {
                    String msg = "";
                    FlowCodeEnum flowCodeEnum = FlowCodeEnum.getEnumByCode(apply.getFlowCode());
                    if (FlowCodeEnum.ORG_SETTLE.equals(flowCodeEnum)) {
                        msg = "当前教室与宿舍申请已提交 ";
                    } else if (FlowCodeEnum.ORG_QUALIFICATION.equals(flowCodeEnum)) {
                        msg = "当前资质申请已提交 ";
                    } else if (FlowCodeEnum.ORG_ALLOWANCE.equals(flowCodeEnum)) {
                        msg = "当前资助资金申请已提交 ";
                    } else if (FlowCodeEnum.ORG_ACTIVITY.equals(flowCodeEnum)) {
                        msg = "当前学术活动申请已提交 ";
                    }
                    throw new ServiceException(msg + "请勿重复提交");
                }
                // 启动流程
                WorkflowStartResult result = workflowExecuteService.start(apply.getFlowCode());

                // 更新申请表单状态 -> 已提交 流程状态 -> 进行中
                Apply updater = new Apply();
                updater.setId(applyId);
                updater.setProcessNo(result.getProcessNo());
                updater.setFlowStatus(FlowStatusEnum.ING.getCode());
                updater.setApplyStatus(ApplyStatusEnum.SUBMIT.getCode());
                if (apply.getApplyDate() == null) {
                    updater.setApplyDate(LocalDate.now());
                }
                updater.setApplyUser(SecurityContextHolder.getUserId());
                this.updateById(updater);
                flag = Boolean.TRUE;
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        return flag;
    }

    @Override
    public boolean removeApply(Long id) {
        Apply apply = this.getById(id);
        apply.setDeleted(DeleteEnum.DELETED.getValue());
        return this.updateById(apply);
    }

    @Override
    public ApplyByWorkflowNodeDTO selectApplyByWorkflowNode(IdRequest request) {
        ApplyByWorkflowNodeDTO applyByWorkflowNodeDTO = new ApplyByWorkflowNodeDTO();
        List<WorkflowInstanceNodeResponse> workflowInstanceNodeResponses =
                workflowInstanceNodeMapper.selectByApplyId(request.getId());
        if (CollectionUtils.isNotEmpty(workflowInstanceNodeResponses)) {
            List<WorkflowInstanceNodeResponse> workflowInstanceNodeResponseList =
                    selectApproverInfo(workflowInstanceNodeResponses);
            applyByWorkflowNodeDTO.setWorkflowInstanceNodeResponses(workflowInstanceNodeResponseList);
        }

        return applyByWorkflowNodeDTO;
    }

    /**
     * 查询审批人角色名称或用户名称
     *
     * @param workflowInstanceNodeResponses
     */
    @Override
    public List<WorkflowInstanceNodeResponse> selectApproverInfo(List<WorkflowInstanceNodeResponse> workflowInstanceNodeResponses) {

        List<WorkflowInstanceNodeResponse> workflowInstanceNodeResponseList =
                workflowInstanceNodeResponses.stream().filter(l -> l.getStatus().equals(FlowNodeStatusEnum.NOT_START.getCode())
                        || l.getStatus().equals(FlowNodeStatusEnum.IN_PROGRESS.getCode())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(workflowInstanceNodeResponseList)) {
            //未审核的数据查询ManagerRef数据
            workflowInstanceNodeResponseList = selectApproverInfoV2(workflowInstanceNodeResponseList);
        }

        workflowInstanceNodeResponses =
                workflowInstanceNodeResponses.stream().filter(l -> l.getStatus().equals(FlowNodeStatusEnum.PASS.getCode())
                        || l.getStatus().equals(FlowNodeStatusEnum.NOT_PASS.getCode())).collect(Collectors.toList());
        //已审核的数据查询UpdateBy数据
        if (CollectionUtils.isEmpty(workflowInstanceNodeResponses)) {
            //如果都未审核,返回未审核集合
            return workflowInstanceNodeResponseList;
        }

        List<Long> userIds =
                workflowInstanceNodeResponses.stream().filter(l -> l.getUpdateBy() != null).map(WorkflowInstanceNodeResponse::getUpdateBy)
                                             .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(userIds)) {
            UserListDTO userListDTO = new UserListDTO();
            userListDTO.setIdList(userIds);
            R<List<UserListResultDTO>> listR = remoteTrainUserService.listUser(userListDTO);
            if (listR.getCode() != Constants.SUCCESS) {
                throw new ServiceException("用户信息查询出错");
            }
            workflowInstanceNodeResponses.forEach(l -> {
                listR.getData().forEach(p -> {
                    if (l.getUpdateBy() != null && l.getUpdateBy().equals(p.getId())) {
                        l.setManagerInfo(p.getRealName());
                    }
                });
            });
        }
        if (CollectionUtils.isNotEmpty(workflowInstanceNodeResponseList)) {
            workflowInstanceNodeResponses.addAll(workflowInstanceNodeResponseList);
        }
        return workflowInstanceNodeResponses;
    }

    private List<WorkflowInstanceNodeResponse> selectApproverInfoV2(List<WorkflowInstanceNodeResponse> workflowInstanceNodeResponses) {

        List<String> roles =
                workflowInstanceNodeResponses.stream().filter(l -> l.getManagerType().equals(ManagerTypeEnum.ROLE.getCode()))
                                             .map(WorkflowInstanceNodeResponse::getManagerRef).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(roles)) {
            RoleRequestDTO roleRequestDTO = new RoleRequestDTO();
            roleRequestDTO.setCodes(roles);
            R<List<RoleDTO>> listR = remoteTrainRoleService.getRoles(roleRequestDTO);
            if (listR.getCode() != Constants.SUCCESS) {
                throw new ServiceException("角色信息查询出错");
            }
            Map<String, String> roleMap = listR.getData().stream().collect(Collectors.toMap(RoleDTO::getCode,
                    RoleDTO::getName, (a, b) -> b));
            workflowInstanceNodeResponses.forEach(l -> {
                if (l.getManagerType().equals(ManagerTypeEnum.ROLE.getCode())) {
                    l.setManagerInfo(roleMap.get(l.getManagerRef()));
                }
            });
        }

        List<String> users =
                workflowInstanceNodeResponses.stream().filter(l -> l.getManagerType().equals(ManagerTypeEnum.USER.getCode()))
                                             .map(WorkflowInstanceNodeResponse::getManagerRef).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(users)) {
            List<Long> userIds = new ArrayList<>();
            users.forEach(l -> userIds.add(Long.valueOf(l)));
            UserListDTO userListDTO = new UserListDTO();
            userListDTO.setIdList(userIds);
            R<List<UserListResultDTO>> listR = remoteTrainUserService.listUser(userListDTO);
            if (listR.getCode() != Constants.SUCCESS) {
                throw new ServiceException("用户信息查询出错");
            }
            workflowInstanceNodeResponses.forEach(l -> {
                if (l.getManagerType().equals(ManagerTypeEnum.USER.getCode())) {
                    listR.getData().forEach(p -> {
                        if (l.getManagerRef().equals(p.getId().toString())) {
                            l.setManagerInfo(p.getRealName());
                        }
                    });
                }
            });
        }
        return workflowInstanceNodeResponses;
    }

    @Override
    public IPage<ApplyPageResponse> selectCreateBy(IPage<ApplyPageResponse> responseIPage) {
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
}
