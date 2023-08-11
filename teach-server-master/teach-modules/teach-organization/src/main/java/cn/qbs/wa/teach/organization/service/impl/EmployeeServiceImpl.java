package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.qbs.wa.teach.basic.api.RemoteAppService;
import cn.qbs.wa.teach.basic.api.RemoteMenuService;
import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.app.ApplicationFullDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.app.ApplicationFullDetailDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.app.ApplicationFullResultDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListResultDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.TreeMenuDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.*;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.organization.entity.*;
import cn.qbs.wa.teach.organization.enums.BusinessTypeEnum;
import cn.qbs.wa.teach.organization.enums.InitDeptEnum;
import cn.qbs.wa.teach.organization.enums.InitRoleEnum;
import cn.qbs.wa.teach.organization.enums.SexEnum;
import cn.qbs.wa.teach.organization.mapper.*;
import cn.qbs.wa.teach.organization.pojo.employee.*;
import cn.qbs.wa.teach.organization.pojo.importrecord.ImportCountResponse;
import cn.qbs.wa.teach.organization.pojo.importrecorddetail.ImportRecordDetailDetailResponse;
import cn.qbs.wa.teach.organization.pojo.organizationmenu.OrganizationMenuVO;
import cn.qbs.wa.teach.organization.service.*;
import cn.qbs.wa.train.logistics.api.RemoteTrainLogisticsEmployeeService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.EmployeeAddRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.EmployeeUpdateRequestDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 职工(Employee)表服务实现类
 *
 * @author makejava
 * @since 2021-11-09 20:11:19
 */
@Slf4j
@Service("employeeService")
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    RemoteTrainLogisticsEmployeeService remoteTrainLogisticsEmployeeService;

    @Autowired
    RemoteUserService remoteUserService;

    @Autowired
    RemoteAppService remoteAppService;

    @Autowired
    RemoteMenuService remoteMenuService;

    @Autowired
    EmployeeRoleService employeeRoleService;

    @Autowired
    EmployeeDeptService employeeDeptService;

    @Autowired
    EmployeeInfoService employeeInfoService;

    @Autowired
    RedisService redisService;

    @Autowired
    DeptService deptService;

    @Autowired
    OrganizationMapper organizationMapper;

    @Autowired
    OrganizationMenuMapper organizationMenuMapper;

    @Autowired
    ImportEmployeeMapper importEmployeeMapper;

    @Autowired
    OrganizationRoleMapper organizationRoleMapper;

    @Autowired
    DeptMapper deptMapper;

    @Resource
    private ImportRecordService importRecordService;

    @Resource
    private ImportRecordDetailService importRecordDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee add(EmployeeAddRequest params) {
        UserAddDTO userAddDTO = new UserAddDTO();
        BeanUtils.copyProperties(params, userAddDTO);

//        UserInfoDTO userInfoDTO = new UserInfoDTO();
//        userInfoDTO.setAccount(params.getPhone());
//        R<UserOneResultDTO> userOneResultDTOR = remoteUserService.checkExistUser(userInfoDTO);
//        if (userOneResultDTOR != null){
//            throw new ServiceException(String.format("该手机号码 %s 已注册", AesUtil.decode(params.getPhone())));
//        }

        R<UserAddResultDTO> userResult = remoteUserService.add(userAddDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        //职工新增
        UserAddResultDTO userAddResultDTO = userResult.getData();
        Employee employee = new Employee();
        BeanUtils.copyProperties(params, employee);
        employee.setUserId(userAddResultDTO.getId());
        employee.setPhone(userAddResultDTO.getAccount());
        Long count = this.lambdaQuery().eq(Employee::getOrgId, params.getOrgId()).eq(Employee::getUserId, employee.getUserId()).count();
        if (count != null && count > 0) {
            throw new ServiceException(String.format("手机号码 %s 已存在", AesUtil.decode(employee.getPhone())));
        }
        this.save(employee);

        //职工部门新增
        saveEmployeeDept(params.getDeptIdList(), employee);
        //职工角色新增
        saveEmployeeRole(params.getRoleIdList(), employee);
        //教务中心职工添加
        //saveTrainEmployee(params.getPhone(),employee);
        return employee;
    }

    private void saveTrainEmployee(String phone,Employee employee) {
        EmployeeAddRequestDTO employeeAddRequestDTO=new EmployeeAddRequestDTO();
        employeeAddRequestDTO.setAccount(phone);
        employeeAddRequestDTO.setPhone(phone);
        employeeAddRequestDTO.setEnabled(employee.getEnabled());
        employeeAddRequestDTO.setOrgId(employee.getOrgId());
        employeeAddRequestDTO.setRealName(employee.getRealName());
        employeeAddRequestDTO.setSex(employee.getSex());
        employeeAddRequestDTO.setId(employee.getId());
        remoteTrainLogisticsEmployeeService.add(employeeAddRequestDTO);
    }

    private void saveEmployeeRole(List<Long> roleIdList, Employee employee) {
        if (CollectionUtils.isNotEmpty(roleIdList)) {
            Long adminRoleId = organizationRoleMapper.getEmployeeRole(employee.getOrgId(), InitRoleEnum.ORG_MASTER.getCode());
            if (roleIdList.contains(adminRoleId)) {
                throw new ServiceException("角色无法授权为机构管理员");
            }
            List<EmployeeRole> employeeRoles = new ArrayList<>();
            for (Long roleId : roleIdList) {
                EmployeeRole employeeRole = new EmployeeRole();
                employeeRole.setEmployeeId(employee.getId());
                employeeRole.setRoleId(roleId);
                employeeRoles.add(employeeRole);
            }
            employeeRoleService.saveBatch(employeeRoles);
        }
    }

    private void saveEmployeeDept(List<Long> deptIdList, Employee employee) {
        if (CollectionUtils.isNotEmpty(deptIdList)) {
            List<EmployeeDept> employeeDeptList = new ArrayList<>();
            for (Long deptId : deptIdList) {
                if (StringUtils.isNotBlank(deptId.toString())) {
                    EmployeeDept employeeDept = new EmployeeDept();
                    employeeDept.setEmployeeId(employee.getId());
                    employeeDept.setDeptId(deptId);
                    employeeDeptList.add(employeeDept);
                }
            }
            employeeDeptService.saveBatch(employeeDeptList);
        }
    }

    @Override
    public IPage<EmployeePageResponse> page(EmployeePageRequest params) {
        if (StringUtils.isNotBlank(params.getSearchContent()) && Validator.isMobile(params.getSearchContent())) {
            params.setSearchContent(AesUtil.encode(params.getSearchContent()));
        }
        if (StringUtils.isNotBlank(params.getPhone())) {
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        IPage<EmployeePageResponse> page = baseMapper.page(params.createMpPage(), params);
        if (params.getRoleId() != null && !page.getRecords().isEmpty()) {
            // 筛选角色情况，多角色无法显示的问题
            List<Long> ids = page.getRecords().stream().map(EmployeePageResponse::getId).collect(Collectors.toList());
            List<EmployeePageResponse> list = baseMapper.listEmployeeRoleName(ids);
            if (!list.isEmpty()) {
                Map<Long, String> map = list.stream().collect(Collectors.toMap(EmployeePageResponse::getId, EmployeePageResponse::getRoleNames));
                for (EmployeePageResponse record : page.getRecords()) {
                    record.setRoleNames(map.get(record.getId()));
                }
            }
        }
        return page;
    }

    @Override
    public EmployeeDetailResponse detail(Serializable id) {
        EmployeeDetailResponse employeeDetailResponse = baseMapper.selectDetailById(id);
        employeeDetailResponse.setDeptIdList(employeeDeptService.listIdByEmployeeId(id));
        IdRequest idRequest = new IdRequest();
        idRequest.setId(employeeDetailResponse.getUserId());
        R<UserOneResultDTO> oneResult = remoteUserService.details(idRequest);
        if (R.FAIL == oneResult.getCode()) {
            throw new ServiceException(oneResult.getMsg());
        }
        UserOneResultDTO oneResultDTO = oneResult.getData();
        if (oneResultDTO != null) {
            employeeDetailResponse.setIdNumber(oneResultDTO.getIdNumber());
            employeeDetailResponse.setEmail(oneResultDTO.getEmail());
            employeeDetailResponse.setHeadImgUrl(oneResultDTO.getHeadImgUrl());
        }
        return employeeDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeUpdateResponse update(EmployeeUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Employee employeeExist = getById(params.getId());
        if (employeeExist == null) {
            throw new ServiceException("无法找到该用户");
        }
        List<OrganizationRole> roles = employeeRoleService.getRole(employeeExist.getId());
        if (CollUtil.isNotEmpty(roles) && roles.stream().anyMatch(r -> InitRoleEnum.ORG_MASTER.getCode().equals(r.getCode()))) {
            // 机构管理员 角色账号不允许修改
            throw new ServiceException("无法修改机构管理员账号信息");
        }
        UserOrgUpdateDTO userOrgUpdateDTO = new UserOrgUpdateDTO();
        userOrgUpdateDTO.setId(employeeExist.getUserId());
        userOrgUpdateDTO.setIdNumber(params.getIdNumber());
        userOrgUpdateDTO.setPassword(params.getPassword());
        userOrgUpdateDTO.setRealName(params.getRealName());
        R<UserOneResultDTO> userResult = remoteUserService.updateOrgUser(userOrgUpdateDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(params, employee);
        //将用户机构id置空，暂不支持用户转机构
        employee.setOrgId(null);
        if (StringUtils.isNotBlank(employee.getPhone())) {
            employee.setPhone(AesUtil.encode(employee.getPhone()));
        }
        boolean flag = this.updateById(employee);

        //职工角色新增
        employeeRoleService.remove(new LambdaQueryWrapper<EmployeeRole>().eq(EmployeeRole::getEmployeeId, params.getId()));
        saveEmployeeRole(params.getRoleIdList(), employee);

        //职工部门新增
        List<EmployeeDept> oldEmployeeDeptList = employeeDeptService.list(new LambdaQueryWrapper<EmployeeDept>().eq(EmployeeDept::getEmployeeId, params.getId()));
        List<Long> oldDeptIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(oldEmployeeDeptList)) {
            oldDeptIdList = oldEmployeeDeptList.stream().map(EmployeeDept::getDeptId).collect(Collectors.toList());
            employeeDeptService.remove(new LambdaQueryWrapper<EmployeeDept>().in(EmployeeDept::getId, oldEmployeeDeptList.stream().map(EmployeeDept::getId).collect(Collectors.toList())));
        }
        saveEmployeeDept(params.getDeptIdList(), employee);
        EmployeeUpdateResponse employeeUpdateResponse = new EmployeeUpdateResponse();
        oldDeptIdList.addAll(params.getDeptIdList());
        employeeUpdateResponse.setDeptIdList(oldDeptIdList);
        //教务中心员工信息更新
        updateTrainEmployee(employee);
        return employeeUpdateResponse;
    }

    private void updateTrainEmployee(Employee employee) {
        EmployeeUpdateRequestDTO employeeUpdateRequestDTO=new EmployeeUpdateRequestDTO();
        employeeUpdateRequestDTO.setEnabled(employee.getEnabled());
        employeeUpdateRequestDTO.setId(employee.getId());
        employeeUpdateRequestDTO.setSex(employee.getSex());
        employeeUpdateRequestDTO.setRealName(employee.getRealName());
        remoteTrainLogisticsEmployeeService.adminUpdate(employeeUpdateRequestDTO);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<EmployeePageResponse> listEmployee(EmployeeListRequest params) {
        if (StringUtils.isNotBlank(params.getSearchContent()) && Validator.isMobile(params.getSearchContent())) {
            params.setSearchContent(AesUtil.encode(params.getSearchContent()));
        }
        return this.baseMapper.listEmployee(params);
    }

    @Override
    public EmployeeLoginPermissionResponse getEmployeePermission(EmployeeLoginPermissionRequest request) {
        EmployeeLoginPermissionResponse employeeLoginPermissionResponse;
        Long userId = SecurityUtils.getUserId();
        List<Employee> employeeList = list(new LambdaQueryWrapper<Employee>().eq(Employee::getUserId, userId).eq(Employee::getEnabled, Constants.DB_TRUE));
        if (CollectionUtils.isNotEmpty(employeeList)) {
            Employee employee = employeeList.get(0);
            employeeLoginPermissionResponse = getEmployeePermission(request.getApplicationTypeId(), employee);
        } else {
            throw new ServiceException("抱歉,该账号没有入驻机构");
        }

        return employeeLoginPermissionResponse;
    }

    @Override
    public EmployeeLoginPermissionResponse getEmployeePermission(Long applicationTypeId, Employee employee) {
        EmployeeLoginPermissionResponse response = new EmployeeLoginPermissionResponse();
        response.setRealName(SecurityContextHolder.getUserName());

        //获取应用
        List<ApplicationFullResultDTO> appList = getRemoteApplication(applicationTypeId);
        List<Long> appIdList = initAppIdList(appList);
        List<OrganizationRole> organizationRoles = getRole(employee.getId());
        List<MenuListResultDTO> menuList = null;
        if (CollectionUtils.isNotEmpty(organizationRoles)) {
            List<Long> roleIdList = organizationRoles.stream().map(OrganizationRole::getId).collect(Collectors.toList());
            response.setRoles(organizationRoles.stream().map(OrganizationRole::getName).collect(Collectors.toList()));
            List<OrganizationMenuVO> menuVOList = null;
            // 获取菜单
            if (organizationRoles.stream().anyMatch(or -> InitRoleEnum.ORG_MASTER.getCode().equals(or.getCode()))) {
                // 如果是超级管理员，则列出所有菜单
                List<OrganizationMenu> menus = organizationMenuMapper.selectList(Wrappers.<OrganizationMenu>lambdaQuery().select(OrganizationMenu::getMenuId).eq(OrganizationMenu::getOrgId, employee.getOrgId()));
                if (CollUtil.isNotEmpty(menus)) {
                    menuVOList = menus.stream().map(e -> new OrganizationMenuVO(e.getMenuId())).distinct().collect(Collectors.toList());
                }
            } else {
                menuVOList = organizationMenuMapper.getMenuByRoleIdList(roleIdList);
            }
            if (CollectionUtils.isNotEmpty(menuVOList)) {
                menuList = getRemoteMenu(menuVOList);
                //组装应用菜单
                if (CollectionUtils.isNotEmpty(menuList) && CollectionUtils.isNotEmpty(appIdList)) {
                    List<MenuListResultDTO> roleMenuVOList = menuList.stream().filter(i -> appIdList.contains(i.getAppId())).collect(Collectors.toList());
                    List<TreeMenuDTO> treeMenuResponses = initTreeMenu(roleMenuVOList);
                    initAppMenu(appList, treeMenuResponses);
                }
            }
            response.setAppList(filterAppList(appList));
        }

        setContextOrgAndEmployeeId(employee, menuList);

        Organization organization = organizationMapper.selectById(employee.getOrgId());
        if (organization != null) {
            if (Constants.DB_FAIL.equals(organization.getEnabled())) {
                throw new ServiceException("抱歉,该机构已停用");
            }
            response.setOrgName(organization.getName());
            response.setPcLogoUrl(organization.getPcLogoUrl());
            response.setH5LogoUrl(organization.getH5LogoUrl());
            response.setOrgId(organization.getId());
        }
        return response;
    }

    /**
     * 上下文注入orgId
     */
    private void setContextOrgAndEmployeeId(Employee employee, List<MenuListResultDTO> menuList) {
        Long orgId = employee.getOrgId();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.setOrgId(orgId);
        loginUser.setEmployeeId(employee.getId());
        loginUser.setUsername(employee.getRealName());
        loginUser.getSysUser().setOrgId(orgId);
        loginUser.getSysUser().setEmployeeId(employee.getId());
        loginUser.getSysUser().setUserName(employee.getRealName());
        if (CollUtil.isNotEmpty(menuList)) {
            Set<String> permission = menuList.stream().map(MenuListResultDTO::getPermission).collect(Collectors.toSet());
            loginUser.setPermissions(permission);
        }
        String userKey = getTokenKey(SecurityUtils.getUserKey());
        long expireTime = CacheConstants.EXPIRATION;
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private List<OrganizationRole> getRole(Long employeeId) {
        List<EmployeeRole> employeeRoleList = employeeRoleService.list(new LambdaQueryWrapper<EmployeeRole>().eq(EmployeeRole::getEmployeeId, employeeId));
        List<Long> roleIdList = employeeRoleList.stream().map(EmployeeRole::getRoleId).collect(Collectors.toList());
        List<OrganizationRole> organizationRoles = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roleIdList)) {
            organizationRoles = organizationRoleMapper.selectList(new LambdaQueryWrapper<OrganizationRole>().eq(OrganizationRole::getEnabled, Constants.DB_TRUE).in(OrganizationRole::getId, roleIdList));
        }
        return organizationRoles;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeBatchAddResponse batchAdd(List<EmployeeExcelAddRequest> params) {
        EmployeeBatchAddResponse employeeBatchAddResponse = new EmployeeBatchAddResponse();
        Long orgId = params.get(0).getOrgId();
        //List<ImportEmployee> importEmployeeList = importEmployeeMapper.selectList(new LambdaQueryWrapper<ImportEmployee>().eq(ImportEmployee::getEventId, params.getEventId()).eq(ImportEmployee::getOrgId, orgId));
        if (CollectionUtils.isNotEmpty(params)) {
            //List<OrganizationRole> organizationRoles = organizationRoleMapper.selectList(new LambdaQueryWrapper<OrganizationRole>().eq(OrganizationRole::getCode, InitRoleEnum.EMPLOYEE.getCode()));
            //Long roleId = organizationRoles.get(0).getId();
            List<Long> deptIdList = new ArrayList<>();
            for (EmployeeExcelAddRequest importEmployee : params) {
                if (!importEmployee.getPassed()) {
                    continue;
                }
                EmployeeAddRequest employeeAddRequest = new EmployeeAddRequest();
                BeanUtils.copyProperties(importEmployee, employeeAddRequest);
                employeeAddRequest.setOrgId(orgId);
                employeeAddRequest.setAccount(importEmployee.getPhone());
                if (StringUtils.isNotBlank(importEmployee.getSexName())) {
                    employeeAddRequest.setSex(setSex(importEmployee.getSexName()));
                } else {
                    employeeAddRequest.setSex(SexEnum.OTHER.getSex());
                }
                //获取子部门
                if (StringUtils.isNotBlank(importEmployee.getDeptName())) {
                    String[] split = importEmployee.getDeptName().split("/");
                    String deptName = split[split.length - 1];
                    Dept dept = deptMapper.selectOne(new QueryWrapper<Dept>().eq("dept_name", deptName));
                    if (dept != null) {
                        employeeAddRequest.setDeptIdList(CollUtil.newArrayList(dept.getId()));
                        deptIdList.add(dept.getId());
                    } else {
                        Dept dept1 = deptService.getOne(Wrappers.<Dept>lambdaQuery().eq(Dept::getRemark, "default"));
                        employeeAddRequest.setDeptIdList(CollUtil.newArrayList(dept1.getId()));
                        deptIdList.add(dept1.getId());
                    }
                } else {
                    Dept dept = deptService.getOne(Wrappers.<Dept>lambdaQuery().eq(Dept::getRemark, "default"));
                    employeeAddRequest.setDeptIdList(CollUtil.newArrayList(dept.getId()));
                    deptIdList.add(dept.getId());
                }
                add(employeeAddRequest);
            }
            ImportCountResponse importCountResponse = saveImportRecord(params);
            BeanUtils.copyProperties(importCountResponse, employeeBatchAddResponse);
            employeeBatchAddResponse.setDeptIdList(deptIdList);
        }
        return employeeBatchAddResponse;
    }

    private ImportCountResponse saveImportRecord(List<EmployeeExcelAddRequest> params) {
        //提取失败数据计算
        List<EmployeeExcelAddRequest> failureList = params.stream().filter(i -> !i.getPassed()).collect(Collectors.toList());
        //set返回值
        ImportCountResponse importCountResponse = new ImportCountResponse();
        importCountResponse.setSuccess(params.size() - failureList.size());
        importCountResponse.setFailure(failureList.size());
        //新增导入记录表
        ImportRecord importRecord = new ImportRecord();
        importRecord.setBusinessType(BusinessTypeEnum.EMPLOYEE.getCode());
        importRecord.setOrgId(SecurityContextHolder.getOrgId());
        importRecord.setTotal(params.size());
        importRecord.setFailure(importCountResponse.getFailure());
        importRecord.setSuccess(importCountResponse.getSuccess());
        importRecord.setImportTime(LocalDateTime.now());
        importRecordService.save(importRecord);
        //返回值set导入记录id
        importCountResponse.setId(importRecord.getId());
        //批量新增导入记录详情表
        List<ImportRecordDetailDetailResponse> recordDetailList = params.stream().map(i -> {
            ImportRecordDetailDetailResponse detail = new ImportRecordDetailDetailResponse();
            BeanUtils.copyProperties(i, detail);
            if (StringUtils.isNotBlank(i.getSexName())){
                if (SexEnum.MALE.getName().equals(i.getSexName()) || SexEnum.FEMALE.getName().equals(i.getSexName())){
                    detail.setSex(SexEnum.getEnum(i.getSexName()).getSex());
                }
            }
            detail.setSexName(i.getSexName());
            detail.setPhone(detail.getPhone());
            detail.setDeptName(detail.getDeptName());
            detail.setImportRecordId(importRecord.getId());
            detail.setFailureReason(com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(i.getErrorReasons()) ? StringUtils.strip(i.getErrorReasons().toString(), "[]") : null);
            detail.setSuccess(i.getPassed() ? 1 : 0);
            return detail;
        }).collect(Collectors.toList());
        List<ImportRecordDetail> detailList = BeanUtil.copyToList(recordDetailList, ImportRecordDetail.class);
        importRecordDetailService.saveBatch(detailList);
        return importCountResponse;
    }

    private Integer setSex(String sexName) {
        SexEnum sexEnum = SexEnum.getEnum(sexName);
        if (sexEnum == null) {
            return SexEnum.OTHER.getSex();
        }
        return sexEnum.getSex();
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<Employee> employeeList = new ArrayList<>();
        for (Long employeeId : idList) {
            Employee employee = new Employee();
            employee.setId(employeeId);
            employee.setEnabled(flag);
            employeeList.add(employee);
        }
        updateBatchById(employeeList);
    }

    @Override
    public void resetPassword(IdRequest request) {
        Employee employee = getById(request.getId());
        if (employee != null) {
            UserPasswordAdminDTO userPasswordAdminDTO = new UserPasswordAdminDTO();
            userPasswordAdminDTO.setUserId(employee.getUserId());
            remoteUserService.resetPasswordByAdmin(userPasswordAdminDTO);
        }
    }

    @Override
    public List<EmployeePageResponse> listEmployeeFull(EmployeeListRequest request) {
        return baseMapper.listEmployeeWithRole(request);
    }

    private List<ApplicationFullResultDTO> filterAppList(List<ApplicationFullResultDTO> appList) {
        if (CollectionUtils.isNotEmpty(appList)) {
            //过滤掉没有菜单的应用;
            for (ApplicationFullResultDTO applicationFullResultDTO : appList) {
                List<ApplicationFullDetailDTO> applicationList = applicationFullResultDTO.getApplicationList();
                if (CollectionUtils.isNotEmpty(applicationList)) {
                    List<ApplicationFullDetailDTO> applicationFilterList = new ArrayList<>();
                    for (ApplicationFullDetailDTO applicationFullDetailResponse : applicationList) {
                        if (!applicationFullDetailResponse.getFilter()) {
                            applicationFilterList.add(applicationFullDetailResponse);
                        }
                    }
                    applicationFullResultDTO.setApplicationList(applicationFilterList);
                }
            }
            return appList.stream().filter(i -> CollectionUtils.isNotEmpty(i.getApplicationList())).collect(Collectors.toList());
        }
        return appList;
    }


    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    private List<MenuListResultDTO> getRemoteMenu(List<OrganizationMenuVO> menuVOList) {
        MenuListDTO menuListDTO = new MenuListDTO();
        menuListDTO.setMenuIdList(menuVOList.stream().map(OrganizationMenuVO::getId).collect(Collectors.toList()));
        menuListDTO.setEnabled(Constants.DB_TRUE);
        R<List<MenuListResultDTO>> menuResult = remoteMenuService.listMenu(menuListDTO);
        if (R.FAIL == menuResult.getCode()) {
            throw new ServiceException(menuResult.getMsg());
        }
        return menuResult.getData();
    }

    private List<ApplicationFullResultDTO> getRemoteApplication(Long applicationTypeId) {
        ApplicationFullDTO applicationFullDTO = new ApplicationFullDTO();
        applicationFullDTO.setApplicationTypeId(applicationTypeId);
        R<List<ApplicationFullResultDTO>> appResult = remoteAppService.getFullList(applicationFullDTO);
        if (R.FAIL == appResult.getCode()) {
            throw new ServiceException(appResult.getMsg());
        }
        return appResult.getData();
    }


    private List<Long> initAppIdList(List<ApplicationFullResultDTO> appList) {
        if (CollectionUtils.isNotEmpty(appList)) {
            List<Long> appIdList = new ArrayList<>();
            for (ApplicationFullResultDTO applicationFullResponse : appList) {
                List<ApplicationFullDetailDTO> applicationList = applicationFullResponse.getApplicationList();
                if (CollectionUtils.isNotEmpty(applicationList)) {
                    appIdList.addAll(applicationList.stream().map(ApplicationFullDetailDTO::getAppId).collect(Collectors.toList()));
                }

            }
            return appIdList;
        }
        return null;
    }


    private void initAppMenu(List<ApplicationFullResultDTO> appList, List<TreeMenuDTO> treeMenuResponses) {
        if (CollectionUtils.isNotEmpty(appList)) {
            for (ApplicationFullResultDTO applicationFullResponse : appList) {
                List<ApplicationFullDetailDTO> applicationList = applicationFullResponse.getApplicationList();
                setMenuList(applicationList, treeMenuResponses);

            }
        }

    }

    private void setMenuList(List<ApplicationFullDetailDTO> applicationList, List<TreeMenuDTO> treeMenuResponses) {
        if (CollectionUtils.isNotEmpty(applicationList) && CollectionUtils.isNotEmpty(treeMenuResponses)) {
            for (ApplicationFullDetailDTO applicationFullDetailResponse : applicationList) {
                List<TreeMenuDTO> menuList = new ArrayList<>();
                for (TreeMenuDTO treeMenuResponse : treeMenuResponses) {
                    if (applicationFullDetailResponse.getAppId().equals(treeMenuResponse.getAppId())) {
                        menuList.add(treeMenuResponse);
                    }
                }
                applicationFullDetailResponse.setMenuList(menuList);
            }
        }

    }

    private List<TreeMenuDTO> initTreeMenu(List<MenuListResultDTO> roleMenuVOList) {
        if (CollectionUtils.isNotEmpty(roleMenuVOList)) {
            List<TreeMenuDTO> treeMenuResponses = new ArrayList<>();
            for (MenuListResultDTO roleMenuVO : roleMenuVOList) {
                TreeMenuDTO treeMenuResponse = new TreeMenuDTO();
                BeanUtils.copyProperties(roleMenuVO, treeMenuResponse);
                treeMenuResponse.setMenuName(roleMenuVO.getName());
                treeMenuResponses.add(treeMenuResponse);
            }
            return (List<TreeMenuDTO>) TreeUtil.tree(treeMenuResponses);
        }
        return null;
    }

    @Override
    public EmployeeDetailResponse personalInfoByUserId(Long userId) {
        return this.lambdaQuery().eq(Employee::getUserId, userId).oneOpt().map(employee -> this.detail(employee.getId())).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeUpdateResponse updateRole(EmployeeUpdateRoleRequest params) {
        //职工角色新增
        employeeRoleService.remove(new LambdaQueryWrapper<EmployeeRole>().eq(EmployeeRole::getEmployeeId, params.getId()));
        Employee employee = new Employee();
        employee.setId(params.getId());
        saveEmployeeRole(params.getRoleIdList(), employee);
        return null;
    }

    @Override
    public Boolean changeInfoByPersonal(PersonalInfoUpdateRequest params) {
        Employee employee = this.getById(params.getId());
        if (employee == null) {
            throw new ServiceException("无法找到该用户");
        }
        UserOrgUpdateDTO userOrgUpdateDTO = new UserOrgUpdateDTO();
        BeanUtils.copyProperties(params, userOrgUpdateDTO);
        userOrgUpdateDTO.setId(employee.getUserId());
        R<UserOneResultDTO> userResult = remoteUserService.updateOrgUser(userOrgUpdateDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        Employee update = new Employee();
        BeanUtils.copyProperties(params, update);
        UserOneResultDTO oneResultDTO = userResult.getData();
        if (oneResultDTO != null) {
            update.setPhone(oneResultDTO.getAccount());
        }
        return this.updateById(update);
    }

    @Override
    public Boolean changePhoneByPersonal(PersonalPhoneUpdateRequest params) {
        Employee employee = this.getById(params.getId());
        if (employee == null) {
            throw new ServiceException("无法找到该用户");
        }
        UserOrgUpdateDTO userOrgUpdateDTO = new UserOrgUpdateDTO();
        userOrgUpdateDTO.setPhone(params.getPhone());
        userOrgUpdateDTO.setAccount(params.getPhone());
        userOrgUpdateDTO.setId(employee.getUserId());
        R<UserOneResultDTO> userResult = remoteUserService.updateOrgUser(userOrgUpdateDTO);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        UserOneResultDTO oneResultDTO = userResult.getData();
        if (oneResultDTO != null) {
            Employee update = new Employee();
            update.setId(params.getId());
            update.setPhone(oneResultDTO.getAccount());
            return this.updateById(update);
        }
        return false;
    }

    @Override
    public Boolean changePasswordByPersonal(PersonalPasswordUpdateRequest params) {
        if (!params.getNewPassword().equals(params.getConfirmPassword())) {
            throw new ServiceException("两次输入新密码不一致");
        }

        if (params.getNewPassword().equals(params.getOldPassword())) {
            throw new ServiceException("新旧密码一致，无需更改");
        }
        Employee employee = this.getById(params.getId());
        if (employee == null) {
            throw new ServiceException("无法找到该用户");
        }

        R<UserOneResultDTO> data = remoteUserService.details(new IdRequest(employee.getUserId()));
        UserOneResultDTO user = data.getData();
        if (R.FAIL == data.getCode() || ObjectUtil.isEmpty(user)) {
            throw new ServiceException("无法找到该用户");
        }
        // 密码加密后进行对比
        if (!SecurityUtils.matchesPassword(params.getOldPassword(), user.getPassword())) {
            throw new ServiceException("旧密码不正确");
        }

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setAccount(user.getAccount());
        userPasswordDTO.setPassword(params.getNewPassword());
        R<?> result = remoteUserService.resetPassword(userPasswordDTO);
        if (R.FAIL == result.getCode()) {
            log.error("修改密码失败. userInfo: {}, {}", user, result.getMsg());
            throw new ServiceException(result.getMsg());
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeInfoByPersonalV2(PersonalInfoSpecUpdateRequest params) {
        this.changeInfoByPersonal(params);
        employeeInfoService.lambdaQuery().eq(EmployeeInfo::getEmployeeId, params.getId()).oneOpt().ifPresent(e -> {
            e.setEducation(params.getEducation());
            e.setJob(params.getJob());
            e.setPosition(params.getPosition());
            employeeInfoService.updateById(e);
        });
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee register(EmployeeSpecAddRequest params) {
        Long orgId = params.getOrgId();
        if (CollUtil.isEmpty(params.getDeptIdList())) {
            // 设置默认的部门
            List<Dept> deptList = deptService.lambdaQuery().eq(Dept::getParentId, 0).list();
            Long defaultDeptId;
            if (deptList.isEmpty()) {
                // 空则创建一个默认的部门
                defaultDeptId = initDept(orgId).getId();
            } else {
                defaultDeptId = deptList.stream().filter(d -> InitDeptEnum.ORG_MASTER.getName().equals(d.getDeptName()))
                        .findFirst()
                        .orElseGet(() -> initDept(orgId))
                        .getId();
            }
            params.setDeptIdList(CollUtil.newArrayList(defaultDeptId));
        }
        // 保存职员信息
        Employee employee = this.add(params);
        if (employee.getId() != null) {
            // 保存info
            EmployeeInfo entity = new EmployeeInfo();
            BeanUtils.copyProperties(params, entity);
            entity.setEmployeeId(employee.getId());
            employeeInfoService.save(entity);
        }
        return employee;
    }

    private Dept initDept(Long orgId) {
        Dept dept = new Dept();
        dept.setOrgId(orgId);
        dept.setDeptName(InitDeptEnum.ORG_MASTER.getName());
        deptService.save(dept);
        return dept;
    }

    @Override
    public Long categoryCount(List<Long> categoryIdList) {
        return this.employeeDeptService.lambdaQuery().select(EmployeeDept::getEmployeeId)
                .in(CollectionUtils.isNotEmpty(categoryIdList), EmployeeDept::getDeptId, categoryIdList).count();
    }
}

