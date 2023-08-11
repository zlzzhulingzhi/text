package cn.qbs.wa.train.logistics.service.impl.inner;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.basic.api.RemoteTrainMenuService;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.entity.OrganizationMenu;
import cn.qbs.wa.train.logistics.entity.OrganizationRole;
import cn.qbs.wa.train.logistics.enums.InitRoleEnum;
import cn.qbs.wa.train.logistics.pojo.employee.EmployeeAddRequest;
import cn.qbs.wa.train.logistics.pojo.organization.OrganizationUpdateRequest;
import cn.qbs.wa.train.logistics.pojo.organization.inner.OrganizationInnerAddRequest;
import cn.qbs.wa.train.logistics.service.inner.EmployeeInnerService;
import cn.qbs.wa.train.logistics.service.inner.OrganizationInnerService;
import cn.qbs.wa.train.logistics.mapper.OrganizationMapper;
import cn.qbs.wa.train.logistics.pojo.organization.OrganizationAddRequest;
import cn.qbs.wa.train.logistics.service.OrganizationMenuService;
import cn.qbs.wa.train.logistics.service.OrganizationRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    RemoteTrainMenuService remoteTrainMenuService;

    @Autowired
    EmployeeInnerService employeeInnerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean init(OrganizationInnerAddRequest params) {
        Organization organization = new Organization();
        //判断是否存在同名机构
        checkAddExist(params.getName());
        BeanUtils.copyProperties(params, organization);
        organization.setId(params.getOrgId());
        boolean saveFlag = save(organization);
        //从train_basic获取菜单id
        List<Long> menuIdList=remoteTrainMenuService.getMenuIds().getRemoteData();
        saveOrganizationMenu(menuIdList, organization);
        //往organization_role添加机构管理员
        saveOrganizationRole(organization.getId());
        //添加管理员
        saveEmployee(params);
        return saveFlag;
    }

    private void saveEmployee(OrganizationInnerAddRequest params) {
        EmployeeAddRequest employeeAddRequest=new EmployeeAddRequest();
        employeeAddRequest.setAccount(params.getAccount());
        employeeAddRequest.setPhone(params.getPhone());
        employeeAddRequest.setRealName(params.getRealName());
        employeeAddRequest.setPassword(params.getPassword());
        employeeAddRequest.setUserId(params.getUserId());
        employeeAddRequest.setEmail(params.getEmail());
        employeeAddRequest.setOrgId(params.getOrgId());
        employeeInnerService.add(employeeAddRequest);
    }

    @Override
    public boolean adminUpdate(OrganizationUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        //判断是否存在同名机构
        checkUpdateExist(params.getName(), params.getId());
        Organization organization = new Organization();
        BeanUtils.copyProperties(params, organization);
        boolean flag = this.updateById(organization);
        return flag;
    }

    @Override
    public boolean update(OrganizationUpdateRequest params) {
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

    private void checkAddExist(String orgName) {
        if (StrUtil.isBlank(orgName)) {
            return;
        }
        List<Organization> list = list(new LambdaQueryWrapper<Organization>().eq(Organization::getName, orgName));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("已存在同名的机构");
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

}

