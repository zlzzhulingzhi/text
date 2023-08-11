package cn.qbs.wa.teach.organization.service.impl.inner;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.basic.api.RemoteMenuService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.organization.entity.*;
import cn.qbs.wa.teach.organization.enums.InitDeptEnum;
import cn.qbs.wa.teach.organization.enums.InitRoleEnum;
import cn.qbs.wa.teach.organization.mapper.OrganizationMapper;
import cn.qbs.wa.teach.organization.pojo.employee.EmployeeAddRequest;
import cn.qbs.wa.teach.organization.pojo.organization.OrganizationDetailResponse;
import cn.qbs.wa.teach.organization.pojo.organization.inner.OrganizationInnerAddRequest;
import cn.qbs.wa.teach.organization.pojo.organization.inner.OrganizationInnerUpdateRequest;
import cn.qbs.wa.teach.organization.service.DeptService;
import cn.qbs.wa.teach.organization.service.OrganizationMenuService;
import cn.qbs.wa.teach.organization.service.OrganizationRoleService;
import cn.qbs.wa.teach.organization.service.inner.EmployeeInnerService;
import cn.qbs.wa.teach.organization.service.inner.OrganizationInnerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织机构(Organization)表服务实现类
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
@RefreshScope
@Slf4j
@Service("organizationInnerService")
public class OrganizationInnerServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationInnerService {

    @Autowired
    OrganizationMenuService organizationMenuService;

    @Autowired
    OrganizationRoleService organizationRoleService;

    @Autowired
    RemoteMenuService remoteMenuService;

    @Autowired
    DeptService deptService;

    @Autowired
    EmployeeInnerService employeeInnerService;

    @Override
    public Long init(OrganizationInnerAddRequest params) {
        params.setEnabled(Constants.DB_TRUE);
        Organization organization = new Organization();
        //判断是否存在同名机构
        checkAddExist(params.getName());
        BeanUtils.copyProperties(params, organization);
        organization.setId(params.getOrgId());
        save(organization);
        //从train_teach_basic获取菜单id
        List<Long> menuIdList=remoteMenuService.getMenuIds().getRemoteData();
        saveOrganizationMenu(menuIdList, organization);
        //往organization_role添加机构管理员
        saveOrganizationRole(organization.getId());
        //往dept添加部门数据
        Dept dept=saveDept(organization.getId());
        List<Long> deptIdList=new ArrayList<>();
        deptIdList.add(dept.getId());
        params.setDeptIdList(deptIdList);
        //添加管理员
        saveEmployee(params);
        deptService.asyncUpdatePeopleCount(params.getDeptIdList(), params.getOrgId());
        return dept.getId();
    }

    private void saveEmployee(OrganizationInnerAddRequest params) {
        EmployeeAddRequest employeeAddRequest=new EmployeeAddRequest();
        employeeAddRequest.setAccount(params.getAccount());
        employeeAddRequest.setPhone(params.getPhone());
        employeeAddRequest.setRealName(params.getRealName());
        employeeAddRequest.setPassword(params.getPassword());
        employeeAddRequest.setUserId(params.getUserId());
        employeeAddRequest.setEmail(params.getEmail());
        employeeAddRequest.setDeptIdList(params.getDeptIdList());
        employeeAddRequest.setOrgId(params.getOrgId());
        employeeInnerService.add(employeeAddRequest);
    }

    private void checkAddExist(String orgName) {
        if (StrUtil.isBlank(orgName)) {
            return;
        }
        List<Organization> list = list(new LambdaQueryWrapper<Organization>().eq(Organization::getName, orgName));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("已存在同名的机构");
        }
    }

    @Override
    public boolean update(OrganizationInnerUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        checkUpdateExist(params.getName(), params.getId());
        Organization organization = new Organization();
        BeanUtils.copyProperties(params, organization);
        boolean flag = this.updateById(organization);
        return flag;
    }

    private void checkUpdateExist(String orgName, Long orgId) {
        if (StrUtil.isBlank(orgName)) {
            return;
        }
        List<Organization> list = list(new LambdaQueryWrapper<Organization>().eq(Organization::getName, orgName).ne(Organization::getId, orgId));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("已存在同名的机构");
        }
    }

    private void saveOrganizationMenu(List<Long> menuIdList, Organization organization) {
        if (CollectionUtils.isNotEmpty(menuIdList)) {
            List<OrganizationMenu> organizationMenus = new ArrayList<>();
            for (Long menuId : menuIdList) {
                OrganizationMenu organizationMenu = new OrganizationMenu();
                organizationMenu.setMenuId(menuId);
                organizationMenu.setOrgId(organization.getId());
                organizationMenus.add(organizationMenu);
            }
            organizationMenuService.saveBatch(organizationMenus);
        }
    }

    private OrganizationRole saveOrganizationRole(Long orgId) {
        OrganizationRole organizationRole = new OrganizationRole();
        organizationRole.setOrgId(orgId);
        organizationRole.setName(InitRoleEnum.ORG_MASTER.getName());
        organizationRole.setCode(InitRoleEnum.ORG_MASTER.getCode());
        organizationRoleService.save(organizationRole);
        return organizationRole;
    }

    private Dept saveDept(Long orgId) {
        Dept dept = new Dept();
        dept.setOrgId(orgId);
        dept.setDeptName(InitDeptEnum.ORG_MASTER.getName());
        dept.setRemark(InitDeptEnum.ORG_MASTER.getCode());
        deptService.save(dept);
        return dept;
    }

    @Override
    public OrganizationDetailResponse detail(Serializable id) {
        OrganizationDetailResponse organizationDetailResponse = baseMapper.selectDetailById(id);
        return organizationDetailResponse;
    }

}

