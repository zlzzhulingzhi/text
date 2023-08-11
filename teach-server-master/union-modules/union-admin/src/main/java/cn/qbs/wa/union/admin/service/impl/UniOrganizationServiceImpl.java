package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.enums.AppCodeEnum;
import cn.qbs.wa.teach.common.core.enums.UniClientCodeEnum;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeService;
import cn.qbs.wa.teach.organization.api.RemoteTeacherOrganizationService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.TeacherOrganizationAddRequestDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.TeacherOrganizationInnerUpdateRequestDTO;
import cn.qbs.wa.train.logistics.api.RemoteTrainLogisticsEmployeeService;
import cn.qbs.wa.train.logistics.api.RemoteTrainLogisticsService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.OrganizationAddRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.OrganizationUpdateRequestDTO;
import cn.qbs.wa.union.admin.entity.*;
import cn.qbs.wa.union.admin.mapper.SystemEmployeeUserMapper;
import cn.qbs.wa.union.admin.mapper.UniApplicationMapper;
import cn.qbs.wa.union.admin.mapper.UniApplicationUserMapper;
import cn.qbs.wa.union.admin.mapper.UniOrganizationMapper;
import cn.qbs.wa.union.admin.pojo.uniorganization.*;
import cn.qbs.wa.union.admin.service.UniOrganizationService;
import cn.qbs.wa.union.admin.service.UniUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 统一机构(UniOrganization)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-12 15:44:59
 */
@Slf4j
@Service("uniOrganizationService")
public class UniOrganizationServiceImpl extends ServiceImpl<UniOrganizationMapper, UniOrganization> implements UniOrganizationService {

    @Resource
    private SystemEmployeeUserMapper systemEmployeeUserMapper;

    @Resource
    private UniUserService uniUserService;

    @Resource
    private UniApplicationUserMapper uniApplicationUserMapper;

    @Resource
    private UniApplicationMapper uniApplicationMapper;

    @Resource
    RemoteTrainLogisticsService remoteTrainLogisticsService;

    @Resource
    RemoteTeacherOrganizationService remoteTeacherOrganizationService;

    @Resource
    RemoteEmployeeService remoteEmployeeService;

    @Resource
    RemoteTrainLogisticsEmployeeService remoteTrainLogisticsEmployeeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(UniOrganizationAddRequest params) {
        checkName(params.getName());
        UniOrganization uniOrganization = new UniOrganization();
        BeanUtils.copyProperties(params, uniOrganization);
        // 根据联系人、联系电话 创建一个机构账号
        SystemEmployeeUser user = createOrgUser(params.getContactPerson(), params.getContactNumber(), params.getContactEmail());
        boolean saveFlag =save(uniOrganization);

        //  远程调用 子服务系统新增机构接口
        //培训中心添加机构信息及超级管理员员工
        teacherOrganizationInit(uniOrganization,user);
        //教务中心添加机构信息及超级管理员员工
        trainLogisticsInit(uniOrganization,user);
        return saveFlag;
    }

    private SystemEmployeeUser createOrgUser(String userName, String phone, String email) {

        SystemEmployeeUser systemEmployeeUser = saveOrUpdateEmpUser(phone, userName, email);
        // 授予培训系统、教务系统权限
        List<String> appCodes = Arrays.asList(AppCodeEnum.TEACH.getCode(), AppCodeEnum.LOGISTICS.getCode());
        grantAppPerms(systemEmployeeUser.getId(), appCodes);
        return systemEmployeeUser;
    }

    /**
     * 授予用户应用权限
     * @param userId   用户ID
     * @param appCodes 应用编号
     */
    private void grantAppPerms(Long userId, List<String> appCodes) {
        List<UniApplication> uniApplicationList = uniApplicationMapper.listByAppCode(appCodes, UniClientCodeEnum.ORG.getCode());
        Set<Long> appIdSet = uniApplicationList.stream().map(UniApplication::getId).collect(Collectors.toSet());
        List<UniApplicationUser> uniApplicationUserList = uniApplicationUserMapper.selectList(Wrappers.<UniApplicationUser>lambdaQuery().select(UniApplicationUser::getUniAppId).eq(UniApplicationUser::getSystemUserId, userId));
        if (CollUtil.isNotEmpty(uniApplicationUserList)) {
            Set<Long> exitAppIdSet = uniApplicationUserList.stream().map(UniApplicationUser::getUniAppId).collect(Collectors.toSet());
            // 取差集
            appIdSet.removeAll(exitAppIdSet);
        }
        for (Long uniAppId : appIdSet) {
            UniApplicationUser uniApplicationUser = new UniApplicationUser();
            uniApplicationUser.setSystemUserId(userId);
            uniApplicationUser.setUniAppId(uniAppId);
            uniApplicationUserMapper.insert(uniApplicationUser);
        }
    }

    private void teacherOrganizationInit(UniOrganization uniOrganization,SystemEmployeeUser user){
        TeacherOrganizationAddRequestDTO teacherOrganizationAddRequestDTO=new TeacherOrganizationAddRequestDTO();
        teacherOrganizationAddRequestDTO.setOrgId(uniOrganization.getId());
        teacherOrganizationAddRequestDTO.setEnabled(uniOrganization.getEnabled());
        teacherOrganizationAddRequestDTO.setName(uniOrganization.getName());
        teacherOrganizationAddRequestDTO.setCategory(uniOrganization.getCategory());
        teacherOrganizationAddRequestDTO.setAccount(user.getAccount());
        teacherOrganizationAddRequestDTO.setPhone(user.getPhone());
        teacherOrganizationAddRequestDTO.setRealName(user.getRealName());
        teacherOrganizationAddRequestDTO.setPassword(user.getPassword());
        teacherOrganizationAddRequestDTO.setUserId(user.getId());
        teacherOrganizationAddRequestDTO.setEmail(user.getEmail());
        //初始化机构并添加超级管理员
        remoteTeacherOrganizationService.init(teacherOrganizationAddRequestDTO).getRemoteData();
    }

    private void trainLogisticsInit(UniOrganization uniOrganization,SystemEmployeeUser user){
        OrganizationAddRequestDTO organizationAddRequestDTO=new OrganizationAddRequestDTO();
        organizationAddRequestDTO.setOrgId(uniOrganization.getId());
        organizationAddRequestDTO.setEnabled(uniOrganization.getEnabled());
        organizationAddRequestDTO.setName(uniOrganization.getName());
        organizationAddRequestDTO.setCategory(uniOrganization.getCategory());
        organizationAddRequestDTO.setAccount(user.getAccount());
        organizationAddRequestDTO.setPhone(user.getPhone());
        organizationAddRequestDTO.setRealName(user.getRealName());
        organizationAddRequestDTO.setPassword(user.getPassword());
        organizationAddRequestDTO.setUserId(user.getId());
        organizationAddRequestDTO.setEmail(user.getEmail());
        //初始化机构并添加超级管理员
        remoteTrainLogisticsService.init(organizationAddRequestDTO);
    }

    private void checkName(String name) {
        Long count = this.lambdaQuery().eq(UniOrganization::getName, name).count();
        if (count != null && count > 0) {
            throw new ServiceException(name + " 名称已存在");
        }
    }

    @Override
    public IPage<UniOrganizationPageResponse> page(UniOrganizationPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public UniOrganizationDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(UniOrganizationUpdateRequest params) {
        UniOrganization one = this.lambdaQuery().eq(UniOrganization::getName, params.getName()).one();
        if (one != null && !one.getId().equals(params.getId())) {
            throw new ServiceException(params.getName() + " 名称已存在");
        }
        //  判断是否变更字段(机构名称、启禁用状态、企业分类)，已变更的情况下，远程调用子服务系统机构更新接口
        UniOrganizationDetailResponse uniOrganizationDetailResponse = baseMapper.selectDetailById(params.getId());
        if (!uniOrganizationDetailResponse.getCategory().equals(params.getCategory()) || !uniOrganizationDetailResponse.getName().equals(params.getName())
                || !uniOrganizationDetailResponse.getEnabled().equals(params.getEnabled())) {
            //修改培训中心机构
            updateTeacherOrganization(params);
            //修改教务中心机构
            updateTrainLogistics(params);
        }
        // 20230309 机构只有唯一一个超级管理员与之绑定。编辑后，若联系人信息变更后需要同步各子系统的机构绑定的信息。
        String contactNumber = uniOrganizationDetailResponse.getContactNumber();
        String contactPerson = uniOrganizationDetailResponse.getContactPerson();
        String contactEmail = uniOrganizationDetailResponse.getContactEmail();
        boolean changeBind = false;
        if (!contactNumber.equals(params.getContactNumber())) {
            // 如果联系人手机号修改了，需要变更机构绑定的管理员
            changeBind = true;
        }
        if (changeBind || !contactPerson.equals(params.getContactPerson()) || !StrUtil.equals(contactEmail,params.getContactEmail())) {
            // 修改用户信息
            SystemEmployeeUser systemEmployeeUser = saveOrUpdateEmpUser(changeBind, params.getContactNumber(), params.getContactPerson(), params.getContactEmail());
            Long oldUserId = null;
            if (changeBind) {
                // 通过手机号获取旧用户ID
                oldUserId = systemEmployeeUserMapper.selectOne(Wrappers.<SystemEmployeeUser>lambdaQuery().select(SystemEmployeeUser::getId)
                        .eq(SystemEmployeeUser::getPhone, AesUtil.encode(contactNumber))).getId();
            }
            // 同步修改各系统的用户信息
            syncUpdateOrgUser(oldUserId, params.getId(), systemEmployeeUser);
        }
        UniOrganization uniOrganization = new UniOrganization();
        BeanUtils.copyProperties(params, uniOrganization);
        return this.updateById(uniOrganization);
    }

    private SystemEmployeeUser saveOrUpdateEmpUser(boolean changeBind, String phone, String realName, String email) {
        SystemEmployeeUser systemEmployeeUser = saveOrUpdateEmpUser(phone, realName, email);
        if (changeBind) {
            // 需要变更机构超级管理员绑定，分配给该用户培训系统、教务系统权限
            List<String> appCodes = Arrays.asList(AppCodeEnum.TEACH.getCode(), AppCodeEnum.LOGISTICS.getCode());
            grantAppPerms(systemEmployeeUser.getId(), appCodes);
        }
        return systemEmployeeUser;
    }

    private void syncUpdateOrgUser(Long oldUserId, Long orgId, SystemEmployeeUser user) {
        cn.qbs.wa.train.logistics.api.pojo.DTO.employee.UpdateBindUserDTO requestDTO = BeanUtil.copyProperties(
                user,
                cn.qbs.wa.train.logistics.api.pojo.DTO.employee.UpdateBindUserDTO.class,
                oldUserId != null ? null : new String[] {"account", "phone", "password"}
        );
        requestDTO.setOldUserId(oldUserId);
        requestDTO.setUserId(user.getId());
        requestDTO.setOrgId(orgId);
        // 教务系统
        remoteTrainLogisticsEmployeeService.updateBindUser(requestDTO);
        // 教学系统
        remoteEmployeeService.updateBindUser(BeanUtil.copyProperties(requestDTO, cn.qbs.wa.teach.organization.api.pojo.DTO.employee.UpdateBindUserDTO.class));
    }

    /**
     * 根据 手机号 获取机构用户信息
     * @param phone    手机号
     * @param realName 真实姓名
     * @param email    邮箱
     * @return 机构用户信息
     */
    @NotNull
    private SystemEmployeeUser saveOrUpdateEmpUser(String phone, String realName, String email) {
        String encode = AesUtil.encode(phone);
        SystemEmployeeUser systemEmployeeUser = systemEmployeeUserMapper.selectOne(Wrappers.<SystemEmployeeUser>lambdaQuery().eq(SystemEmployeeUser::getPhone, encode));
        if (systemEmployeeUser == null) {
            systemEmployeeUser = new SystemEmployeeUser();
            systemEmployeeUser.setAccount(encode);
            systemEmployeeUser.setPhone(encode);
            systemEmployeeUser.setRealName(realName);
            systemEmployeeUser.setEmail(email);
            systemEmployeeUser.setPassword(SecurityUtils.encryptPassword(Constants.INIT_PASSWORD));
            // 创建账号
            UniUser uniUser = uniUserService.addUser(BeanUtil.copyProperties(systemEmployeeUser, UniUser.class));
            systemEmployeeUser.setId(uniUser.getId());
            systemEmployeeUserMapper.insert(systemEmployeeUser);
        } else if (!StrUtil.equals(realName, systemEmployeeUser.getRealName()) || !StrUtil.equals(email, systemEmployeeUser.getEmail())) {
            // 姓名、邮箱发生变化
            SystemEmployeeUser employeeUpdater = new SystemEmployeeUser();
            employeeUpdater.setId(systemEmployeeUser.getId());
            employeeUpdater.setRealName(realName);
            employeeUpdater.setEmail(email);
            uniUserService.updateById(BeanUtil.copyProperties(systemEmployeeUser, UniUser.class));
            systemEmployeeUserMapper.updateById(employeeUpdater);
            systemEmployeeUser.setRealName(realName);
            systemEmployeeUser.setEmail(email);
        }
        return systemEmployeeUser;
    }

    private void updateTrainLogistics(UniOrganizationUpdateRequest params) {
        OrganizationUpdateRequestDTO organizationInnerUpdateRequestDTO=new OrganizationUpdateRequestDTO();
        organizationInnerUpdateRequestDTO.setId(params.getId());
        organizationInnerUpdateRequestDTO.setCategory(params.getCategory());
        organizationInnerUpdateRequestDTO.setEnabled(params.getEnabled());
        organizationInnerUpdateRequestDTO.setName(params.getName());
        remoteTrainLogisticsService.update(organizationInnerUpdateRequestDTO);
    }

    private void updateTeacherOrganization(UniOrganizationUpdateRequest params) {
        TeacherOrganizationInnerUpdateRequestDTO teacherOrganizationInnerUpdateRequestDTO=new TeacherOrganizationInnerUpdateRequestDTO();
        teacherOrganizationInnerUpdateRequestDTO.setId(params.getId());
        teacherOrganizationInnerUpdateRequestDTO.setCategory(params.getCategory());
        teacherOrganizationInnerUpdateRequestDTO.setEnabled(params.getEnabled());
        teacherOrganizationInnerUpdateRequestDTO.setName(params.getName());
        remoteTeacherOrganizationService.update(teacherOrganizationInnerUpdateRequestDTO);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        List<UniOrganization> list = idList.stream().distinct().map(id -> {
            UniOrganization uniOrganization = new UniOrganization();
            uniOrganization.setId(id);
            uniOrganization.setEnabled(flag);
            return uniOrganization;
        }).collect(Collectors.toList());
        updateBatchById(list);
        // 依次远程 启/禁用 对应的机构
        for (UniOrganization uniOrg : list) {
            // 培训中心
            UniOrganizationUpdateRequest teachOrg = new UniOrganizationUpdateRequest();
            teachOrg.setId(uniOrg.getId());
            teachOrg.setEnabled(flag);
            updateTeacherOrganization(teachOrg);
            // 教务中心
            UniOrganizationUpdateRequest logisticOrg = new UniOrganizationUpdateRequest();
            logisticOrg.setId(uniOrg.getId());
            logisticOrg.setEnabled(flag);
            updateTrainLogistics(logisticOrg);
        }
    }
}

