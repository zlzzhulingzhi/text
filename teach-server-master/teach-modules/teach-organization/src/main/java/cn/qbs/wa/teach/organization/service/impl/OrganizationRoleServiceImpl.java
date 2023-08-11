package cn.qbs.wa.teach.organization.service.impl;

import cn.qbs.wa.teach.basic.api.RemoteMenuService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListResultDTO;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.organization.entity.EmployeeRole;
import cn.qbs.wa.teach.organization.entity.OrganizationRole;
import cn.qbs.wa.teach.organization.entity.OrganizationRoleMenu;
import cn.qbs.wa.teach.organization.enums.InitRoleEnum;
import cn.qbs.wa.teach.organization.mapper.EmployeeRoleMapper;
import cn.qbs.wa.teach.organization.mapper.OrganizationMenuMapper;
import cn.qbs.wa.teach.organization.mapper.OrganizationRoleMapper;
import cn.qbs.wa.teach.organization.pojo.organizationmenu.OrganizationMenuVO;
import cn.qbs.wa.teach.organization.pojo.organizationrole.*;
import cn.qbs.wa.teach.organization.pojo.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import cn.qbs.wa.teach.organization.service.OrganizationRoleMenuService;
import cn.qbs.wa.teach.organization.service.OrganizationRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【机构角色】(OrganizationRole)表服务实现类
 *
 * @author makejava
 * @since 2021-11-10 08:42:57
 */
@Slf4j
@Service("organizationRoleService")
public class OrganizationRoleServiceImpl extends ServiceImpl<OrganizationRoleMapper, OrganizationRole> implements OrganizationRoleService {

    @Autowired
    OrganizationRoleMenuService organizationRoleMenuService;

    @Autowired
    OrganizationMenuMapper organizationMenuMapper;

    @Autowired
    EmployeeRoleMapper employeeRoleMapper;

    @Autowired
    RemoteMenuService remoteMenuService;

    @Override
    @Transactional
    public boolean add(OrganizationRoleAddRequest params) {
        OrganizationRole organizationRole = new OrganizationRole();
        BeanUtils.copyProperties(params, organizationRole);
        organizationRole.setOrgId(SecurityUtils.getLoginUser().getOrgId());
        organizationRole.setPriority(1);
        boolean flag = this.save(organizationRole);
        saveOrganizationRoleMenu(params.getMenuIdList(), organizationRole.getId());
        return flag;
    }

    private void saveOrganizationRoleMenu(List<Long> menuIdList, Long roleId) {
        if (CollectionUtils.isNotEmpty(menuIdList)) {
            List<OrganizationRoleMenu> organizationRoleMenus = new ArrayList<>();
            for (Long menuId : menuIdList) {
                OrganizationRoleMenu organizationRoleMenu = new OrganizationRoleMenu();
                organizationRoleMenu.setMenuId(menuId);
                organizationRoleMenu.setRoleId(roleId);
                organizationRoleMenus.add(organizationRoleMenu);
            }
            organizationRoleMenuService.saveBatch(organizationRoleMenus);
        }
    }

    @Override
    public IPage<OrganizationRolePageResponse> page(OrganizationRolePageRequest params) {
        IPage<OrganizationRolePageResponse> page = baseMapper.page(params.createMpPage(), params);
        getAndSetMenuNames(page.getRecords());
        return page;
    }

    private void getAndSetMenuNames(List<OrganizationRolePageResponse> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> menuIdList = setMenuIdList(records);
            if (CollectionUtils.isNotEmpty(menuIdList)) {
                MenuListDTO menuListDTO = new MenuListDTO();
                menuListDTO.setMenuIdList(menuIdList);
                menuListDTO.setEnabled(Constants.DB_TRUE);
                R<List<MenuListResultDTO>> menuResultList = remoteMenuService.listMenu(menuListDTO);
                if (R.FAIL == menuResultList.getCode()) {
                    throw new ServiceException(menuResultList.getMsg());
                }
                List<MenuListResultDTO> menuListResultDTOList = menuResultList.getData();
                if (CollectionUtils.isNotEmpty(menuListResultDTOList)) {
                    for (OrganizationRolePageResponse record : records) {
                        setMenuName(record, menuListResultDTOList);
                    }
                }
            }
        }
    }

    private List<Long> setMenuIdList(List<OrganizationRolePageResponse> records) {
        List<Long> menuIdTotalList = new ArrayList<>();
        for (OrganizationRolePageResponse record : records) {
            List<Long> menuIdList = new ArrayList<>();
            String menuIdStr = record.getMenuIdStr();
            if (StringUtils.isNotBlank(menuIdStr)) {
                String[] split = menuIdStr.split(",");
                for (String menuId : split) {
                    menuIdList.add(Long.valueOf(menuId));
                    menuIdTotalList.add(Long.valueOf(menuId));
                }
                record.setMenuIdList(menuIdList);
            }
        }
        return menuIdTotalList;
    }


    private void setMenuName(OrganizationRolePageResponse response, List<MenuListResultDTO> menuListResultDTOList) {
        List<Long> menuIdList = response.getMenuIdList();
        if (CollectionUtils.isNotEmpty(menuIdList)) {
            String menuNames = menuListResultDTOList.stream().filter(i -> menuIdList.contains(i.getId())).map(MenuListResultDTO::getName).collect(Collectors.joining(","));
            response.setMenuNames(menuNames);
        }
    }

    @Override
    public OrganizationRoleDetailResponse detail(Long id) {
        OrganizationRoleDetailResponse organizationRoleDetailResponse = baseMapper.selectDetailById(id);
        List<OrganizationMenuVO> menuByRoleIdList = organizationMenuMapper.getMenuByRoleIdList(Arrays.asList(id));
        if (CollectionUtils.isNotEmpty(menuByRoleIdList)) {
            organizationRoleDetailResponse.setMenuIdList(menuByRoleIdList.stream().map(OrganizationMenuVO::getId).collect(Collectors.toList()));
        }
        return organizationRoleDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(OrganizationRoleUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        OrganizationRole organizationRole = new OrganizationRole();
        BeanUtils.copyProperties(params, organizationRole);
        organizationRoleMenuService.remove(new LambdaQueryWrapper<OrganizationRoleMenu>().eq(OrganizationRoleMenu::getRoleId, params.getId()));
        saveOrganizationRoleMenu(params.getMenuIdList(), params.getId());
        return this.updateById(organizationRole);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("id数组为空");
        }
        List<OrganizationRole> list = list(new LambdaQueryWrapper<OrganizationRole>().in(OrganizationRole::getId, idList));
        if (CollectionUtils.isNotEmpty(list)) {
            List<OrganizationRole> initList = list.stream().filter(i -> StringUtils.isNotBlank(i.getCode())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(initList)){
                throw new ServiceException("抱歉,初始角色不能删除");
            }
            // 若角色已使用不能删除
            List<Long> ownRoleIds = list.stream().filter(i -> StringUtils.isBlank(i.getCode())).map(OrganizationRole::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ownRoleIds)) {
                Long count = employeeRoleMapper.selectCount(Wrappers.<EmployeeRole>lambdaQuery().in(EmployeeRole::getRoleId, ownRoleIds));
                if (count != null && count > 0) {
                    throw new ServiceException("抱歉,角色存在用户,无法删除");
                }
            }
        }
        return this.removeByIds(idList);
    }

    @Override
    public List<OrganizationRolePageResponse> listRole(OrganizationRoleListRequest params) {
        List<OrganizationRole> roleList = this.baseMapper.listRole(params);
        if (CollectionUtils.isNotEmpty(roleList)) {
            List<OrganizationRolePageResponse> list = new ArrayList<>();
            for (OrganizationRole organizationRole : roleList) {
                // 不能选择 机构管理员，机构管理员只有一个
                if (InitRoleEnum.ORG_MASTER.getCode().equals(organizationRole.getCode())) {
                    continue;
                }
                OrganizationRolePageResponse organizationRolePageResponse = new OrganizationRolePageResponse();
                BeanUtils.copyProperties(organizationRole, organizationRolePageResponse);
                list.add(organizationRolePageResponse);
            }
            return list;
        }
        return null;
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<OrganizationRole> organizationRoles = new ArrayList<>();
        for (Long roleId : idList) {
            OrganizationRole organizationRole = new OrganizationRole();
            organizationRole.setId(roleId);
            organizationRole.setEnabled(flag);
            organizationRoles.add(organizationRole);
        }
        updateBatchById(organizationRoles);
    }

    @Override
    public List<OrgDeptOrRoleResponseDTO> getOrgRole(Serializable id) {
        return this.baseMapper.getOrgRole(id);
    }
}

