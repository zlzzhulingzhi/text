package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.train.logistics.entity.*;
import cn.qbs.wa.train.logistics.enums.InitRoleEnum;
import cn.qbs.wa.train.logistics.enums.OrgCategoryEnum;
import cn.qbs.wa.train.logistics.mapper.OrganizationMapper;
import cn.qbs.wa.train.logistics.pojo.dept.TreeDeptResponse;
import cn.qbs.wa.train.logistics.pojo.organization.*;
import cn.qbs.wa.train.logistics.pojo.organizationmenu.OrganizationMenuListRequest;
import cn.qbs.wa.train.logistics.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 组织机构(Organization)表服务实现类
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
@Slf4j
@Service("organizationService")
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    @Value("#{'${system.disableHostPrefix:}'.empty ? null : '${system.disableHostPrefix:}'.split(',')}")
    private List<String> disableHostPrefix;

    @Autowired
    OrganizationMenuService organizationMenuService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    OrganizationRoleService organizationRoleService;

    @Autowired
    OrganizationRoleMenuService organizationRoleMenuService;

    @Autowired
    EmployeeRoleService employeeRoleService;

    @Autowired
    RedisService redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(OrganizationAddRequest params) {
        Organization organization = new Organization();
        checkAddExist(params.getName());
        BeanUtils.copyProperties(params, organization);
        boolean saveFlag = save(organization);
        saveOrganizationMenu(params.getMenuIdList(), organization);
        saveOrganizationRoles(organization.getId(), params.getMenuIdList());
        saveDept(organization.getId());
        if (Constants.DB_TRUE.equals(params.getEnabled())) {
            saveDomainMapping(organization.getId(), organization.getDomain());
        }
        return saveFlag;
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

    private void checkUpdateExist(String orgName, Long orgId) {
        if (StrUtil.isBlank(orgName)) {
            return;
        }
        List<Organization> list = list(new LambdaQueryWrapper<Organization>().eq(Organization::getName, orgName).ne(Organization::getId, orgId));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new ServiceException("已存在同名的机构");
        }
    }

    private String changeDomain(String domain, Long orgId, boolean enableFlag) {
        if (enableFlag && StrUtil.isBlank(domain)) {
            return null;
        }
        Organization org = this.getById(orgId);
        if (org == null) {
            throw new ServiceException("机构不存在");
        }
        String oldDomain = org.getDomain();
        // 禁用的情况下，删除映射
        if (!enableFlag) {
            if (StrUtil.isNotBlank(oldDomain)) {
                removeDomainMapping(oldDomain);
            }
            return null;
        }
        // 域名变更
        if (!domain.equals(oldDomain)) {
            // 校验域名
            validDomain(domain);
            return StrUtil.isBlank(oldDomain) ? domain : oldDomain;
        }
        // 判断是否存在，不存在则更新至redis
        Boolean hasKey = redisService.hasCacheMapKey(CacheConstants.TENANT_HOST_MAPPING, domain);
        if (Boolean.FALSE.equals(hasKey)) {
            saveDomainMapping(orgId, domain);
        }
        return null;
    }

    private String generateDomain(String domain) {
        if (StrUtil.isBlank(domain)) {
            domain = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.PAY_DATE_FORMAT));
        } else {
            // 校验域名
            validDomain(domain);
        }
        return domain;
    }

    private void validDomain(String domain) {
        if (!Pattern.matches("^[a-z0-9]{1,30}$", domain)) {
            throw new ServiceException("域名只能使用英文字母（a~z）、数字（0~9）并且长度不超过30个字符");
        }
        if (CollUtil.isNotEmpty(disableHostPrefix) && disableHostPrefix.contains(domain.toLowerCase())) {
            throw new ServiceException(domain + " 域名已被使用");
        }
        long count = this.count(new LambdaQueryWrapper<Organization>().eq(Organization::getDomain, domain.toLowerCase()));
        if (count > 0) {
            throw new ServiceException(domain + " 域名已被使用");
        }
    }

    private void saveDomainMapping(Long orgId, String domain) {
        try {
            redisService.setCacheMapValue(CacheConstants.TENANT_HOST_MAPPING, domain, orgId.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void updateDomainMapping(Long orgId, String oldDomain, String newDomain) {
        try {
            // 删除旧映射，新增新映射
            redisService.deleteCacheMapKey(CacheConstants.TENANT_HOST_MAPPING, oldDomain);
            redisService.setCacheMapValue(CacheConstants.TENANT_HOST_MAPPING, newDomain, orgId.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void removeDomainMapping(String domain) {
        try {
            // 删除映射
            redisService.deleteCacheMapKey(CacheConstants.TENANT_HOST_MAPPING, domain);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void saveDept(Long orgId) {
       /* Dept dept = new Dept();
        dept.setOrgId(orgId);
        dept.setDeptName(InitDeptEnum.ORG_MASTER.getName());
        dept.setRemark(InitDeptEnum.ORG_MASTER.getCode());
        deptService.save(dept);*/
    }

    private OrganizationRole saveOrganizationRole(Long orgId) {
        OrganizationRole organizationRole = new OrganizationRole();
        organizationRole.setOrgId(orgId);
        organizationRole.setName(InitRoleEnum.ORG_MASTER.getName());
        organizationRole.setCode(InitRoleEnum.ORG_MASTER.getCode());
        organizationRoleService.save(organizationRole);
        return organizationRole;
    }

    private List<OrganizationRole> saveOrganizationRoles(Long orgId, List<Long> menuIdList) {

        InitRoleEnum[] roleEnums = InitRoleEnum.values();
        List<OrganizationRole> organizationRoles = new ArrayList<>();
        for (InitRoleEnum roleEnum : roleEnums) {
            OrganizationRole organizationRole = new OrganizationRole();
            organizationRole.setOrgId(orgId);
            organizationRole.setName(roleEnum.getName());
            organizationRole.setCode(roleEnum.getCode());
            organizationRoles.add(organizationRole);
        }
        organizationRoleService.saveBatch(organizationRoles);
        OrganizationMenuListRequest request = new OrganizationMenuListRequest();
        request.setOrgId(orgId);
        OrganizationRole role = organizationRoles.get(0);
        List<OrganizationRoleMenu> organizationRoleMenus = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(menuIdList)) {
            for (Long menuId : menuIdList) {
                OrganizationRoleMenu organizationRoleMenu = new OrganizationRoleMenu();
                organizationRoleMenu.setOrgId(orgId);
                organizationRoleMenu.setMenuId(menuId);
                organizationRoleMenu.setRoleId(role.getId());
                organizationRoleMenus.add(organizationRoleMenu);
            }
            organizationRoleMenuService.saveBatch(organizationRoleMenus);
        }
        return organizationRoles;
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

    @Override
    public IPage<OrganizationPageResponse> page(OrganizationPageRequest params) {
        IPage<OrganizationPageResponse> page = baseMapper.page(params.createMpPage(), params);
        return page;
    }

    @Override
    public IPage<OrganizationPageResponse> shopGetOrgPage(OrganizationPageRequest params) {
        IPage<OrganizationPageResponse> page = baseMapper.page(params.createMpPage(), params);
        return page;
    }

    @Override
    public OrganizationDetailResponse detail(Serializable id) {
        OrganizationDetailResponse organizationDetailResponse = baseMapper.selectDetailById(id);
        List<OrganizationMenu> organizationMenuList = organizationMenuService.adminListByOrgId(id);
        if (CollectionUtils.isNotEmpty(organizationMenuList)) {
            organizationDetailResponse.setMenuIdList(organizationMenuList.stream().map(OrganizationMenu::getMenuId).collect(Collectors.toList()));
        }
        return organizationDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adminUpdate(OrganizationUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        checkUpdateExist(params.getName(), params.getId());
        Organization organization = new Organization();
        BeanUtils.copyProperties(params, organization);
        boolean flag = this.updateById(organization);
        organizationMenuService.remove(new LambdaQueryWrapper<OrganizationMenu>().eq(OrganizationMenu::getOrgId, params.getId()));
        saveOrganizationMenu(params.getMenuIdList(), organization);
        //updateOrganizationMasterRoleMenu(params.getId(), (params.getMenuIdList()));
        removeOrganizationRoleMenu(params.getMenuIdList(), organization);
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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

    private void updateOrganizationMasterRoleMenu(Long orgId, List<Long> menuIdList) {
        List<OrganizationRole> list = organizationRoleService.list(new LambdaQueryWrapper<OrganizationRole>().eq(OrganizationRole::getCode, InitRoleEnum.ORG_MASTER.getCode()));
        if (CollectionUtils.isNotEmpty(list)) {
            OrganizationRole organizationMaster = list.get(0);
            organizationRoleMenuService.remove(new LambdaQueryWrapper<OrganizationRoleMenu>().eq(OrganizationRoleMenu::getRoleId, organizationMaster.getId()));
            List<OrganizationRoleMenu> organizationRoleMenus = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(menuIdList)) {
                for (Long menuId : menuIdList) {
                    OrganizationRoleMenu organizationRoleMenu = new OrganizationRoleMenu();
                    organizationRoleMenu.setOrgId(orgId);
                    organizationRoleMenu.setMenuId(menuId);
                    organizationRoleMenu.setRoleId(organizationMaster.getId());
                    organizationRoleMenus.add(organizationRoleMenu);
                }
                organizationRoleMenuService.saveBatch(organizationRoleMenus);
            }
        }

    }

    private void removeOrganizationRoleMenu(List<Long> menuIdList, Organization organization) {

        List<OrganizationRoleMenu> list = organizationRoleMenuService.listByAdmin(organization.getId());
        if (CollectionUtils.isNotEmpty(list)) {
            List<OrganizationRoleMenu> delRoleMenuList = list.stream().filter(i -> !menuIdList.contains(i.getMenuId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(delRoleMenuList)) {
                organizationRoleMenuService.deleteByAdminIds(delRoleMenuList.stream().map(OrganizationRoleMenu::getId).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        List<Organization> organizations = this.listByIds(idList);
        boolean remove = this.removeByIds(idList);
        if (!organizations.isEmpty()) {
            List<String> domains = organizations.stream().filter(e -> StrUtil.isNotBlank(e.getDomain())).map(Organization::getDomain).collect(Collectors.toList());
            redisService.deleteCacheMapKey(CacheConstants.TENANT_HOST_MAPPING, domains);
        }
        return remove;
    }

    @Override
    public List<OrganizationDeptResponse> orgDeptList() {
        List<OrganizationDeptResponse> list = baseMapper.getOrgDeptList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (OrganizationDeptResponse organizationDeptResponse : list) {
                List<TreeDeptResponse> deptList = organizationDeptResponse.getDeptList();
                if (CollectionUtils.isNotEmpty(deptList)) {
                    organizationDeptResponse.setDeptList((List<TreeDeptResponse>) TreeUtil.tree(deptList));
                }
            }
        }
        return list;
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<Organization> organizations = new ArrayList<>();
        for (Long roleId : idList) {
            Organization organization = new Organization();
            organization.setId(roleId);
            organization.setEnabled(flag);
            organizations.add(organization);
        }
        updateBatchById(organizations);

        List<Organization> orgs = this.listByIds(idList);
        if (!orgs.isEmpty()) {
            if (Constants.DB_FAIL.equals(flag)) {
                List<String> domains = organizations.stream().filter(e -> StrUtil.isNotBlank(e.getDomain())).map(Organization::getDomain).collect(Collectors.toList());
                if (!domains.isEmpty()) {
                    redisService.deleteCacheMapKey(CacheConstants.TENANT_HOST_MAPPING, domains);
                }
            } else {
                Map<String, Long> map = organizations.stream().filter(e -> StrUtil.isNotBlank(e.getDomain())).collect(Collectors.toMap(Organization::getDomain, Organization::getId));
                map.forEach((k, v) -> redisService.setCacheMapValue(CacheConstants.TENANT_HOST_MAPPING, k, v));
            }
        }
    }

    @Override
    public List<OrganizationSelectListResponse> listOrg() {
        return this.baseMapper.listOrg();
    }

    @Override
    public List<TreeDeptResponse> orgDeptTreeList() {
        List<Organization> list = this.lambdaQuery().list();
        return list.stream().sorted(Comparator.comparingInt(Organization::getSort)).map(e -> {
            TreeDeptResponse response = new TreeDeptResponse();
            response.setName(e.getName());
            response.setOrgId(e.getId());
            response.setDeptName(e.getName());
            response.setEnabled(e.getEnabled());
            return response;
        }).collect(Collectors.toList());
    }

    /*@Override
    public List<TreeDeptResponse> orgDeptTreeList() {
        List<Organization> orgList = this.lambdaQuery().orderByAsc(Organization::getSort).list();
        DeptListRequest deptListRequest = new DeptListRequest();
        TreeDeptTotalResponse treeDeptTotalResponse = this.deptService.treeListByOrgId(null, deptListRequest);
        Map<Long, List<TreeDeptResponse>> orgDeptTreeMap = treeDeptTotalResponse.getDeptList().stream().collect(Collectors.groupingBy(TreeDeptResponse::getOrgId));

        List<TreeDeptResponse> orgDeptTreeList = orgList.stream().map(o -> {
            TreeDeptResponse orgTree = new TreeDeptResponse();
            orgTree.setOrgId(o.getId());
            orgTree.setName(o.getName());
            orgTree.setDeptName(o.getName());
            orgTree.setFullName(o.getName());
            orgTree.setEnabled(o.getEnabled());
            orgTree.setStudentCount(0);
            List<TreeDeptResponse> deptTreeList = orgDeptTreeMap.get(o.getId());
            orgTree.setChildren(deptTreeList);
            orgTree.setHasChildren(CollectionUtils.isNotEmpty(deptTreeList) ? 1 : 0);

            Integer peopleCountTotal = 0;
            if (CollectionUtils.isNotEmpty(deptTreeList)) {
                peopleCountTotal = deptTreeList.stream().collect(Collectors.summingInt(TreeDeptResponse::getPeopleCountTotal));
            }
            orgTree.setPeopleCountTotal(peopleCountTotal);

            Integer studentCountTotal = 0;
            if (CollectionUtils.isNotEmpty(deptTreeList)) {
                studentCountTotal = deptTreeList.stream().collect(Collectors.summingInt(TreeDeptResponse::getStudentCountTotal));
            }
            orgTree.setStudentCountTotal(studentCountTotal);

            appendFullname(o.getName(), deptTreeList);

            return orgTree;
        }).collect(Collectors.toList());

        return orgDeptTreeList;
    }*/

    private void appendFullname(String name, List<TreeDeptResponse> deptTreeList) {
        if (CollectionUtils.isNotEmpty(deptTreeList)) {
            deptTreeList.forEach(i -> {
                i.setFullName(name + "/" + i.getFullName());
                this.appendFullname(name, i.getChildren());
            });
        }
    }

}

