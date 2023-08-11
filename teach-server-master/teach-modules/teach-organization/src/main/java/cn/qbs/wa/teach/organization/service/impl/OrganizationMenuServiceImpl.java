package cn.qbs.wa.teach.organization.service.impl;

import cn.qbs.wa.teach.basic.api.RemoteMenuService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListResultDTO;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.organization.entity.OrganizationMenu;
import cn.qbs.wa.teach.organization.mapper.OrganizationMenuMapper;
import cn.qbs.wa.teach.organization.pojo.organizationmenu.*;
import cn.qbs.wa.teach.organization.service.OrganizationMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【角色菜单关联关系】(OrganizationMenu)表服务实现类
 *
 * @author makejava
 * @since 2021-11-09 20:26:19
 */
@Slf4j
@Service("organizationMenuService")
public class OrganizationMenuServiceImpl extends ServiceImpl<OrganizationMenuMapper, OrganizationMenu> implements OrganizationMenuService {

    @Autowired
    RemoteMenuService remoteMenuService;

    @Override
    public boolean add(OrganizationMenuAddRequest params) {
        OrganizationMenu organizationMenu = new OrganizationMenu();
        BeanUtils.copyProperties(params, organizationMenu);
        return this.save(organizationMenu);
    }

    @Override
    public IPage<OrganizationMenuPageResponse> page(OrganizationMenuPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public OrganizationMenuDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(OrganizationMenuUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        OrganizationMenu organizationMenu = new OrganizationMenu();
        BeanUtils.copyProperties(params, organizationMenu);
        return this.updateById(organizationMenu);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<OrganizationMenuListResponse> listOrganizationMenu(OrganizationMenuListRequest params) {
        List<OrganizationMenu> organizationMenuList = list(new LambdaQueryWrapper<OrganizationMenu>().eq(OrganizationMenu::getOrgId, params.getOrgId()));
        if(CollectionUtils.isNotEmpty(organizationMenuList)){
            List<Long> menuIdList = organizationMenuList.stream().map(OrganizationMenu::getMenuId).collect(Collectors.toList());
            MenuListDTO menuListDTO = new MenuListDTO();
            menuListDTO.setMenuIdList(menuIdList);
            menuListDTO.setEnabled(Constants.DB_TRUE);
            R<List<MenuListResultDTO>> menuListResult = remoteMenuService.listMenu(menuListDTO);
            if(R.FAIL==menuListResult.getCode()){
                throw new ServiceException(menuListResult.getMsg());
            }
            List<MenuListResultDTO> menuListResultDTO = menuListResult.getData();
            if(CollectionUtils.isNotEmpty(menuListResultDTO)){
                List<OrganizationMenuListResponse> responseList=new ArrayList<>();
                for (MenuListResultDTO listResultDTO : menuListResultDTO) {
                    OrganizationMenuListResponse organizationMenuListResponse = new OrganizationMenuListResponse();
                    BeanUtils.copyProperties(listResultDTO,organizationMenuListResponse);
                    responseList.add(organizationMenuListResponse);
                }
                return (List<OrganizationMenuListResponse>) TreeUtil.tree(responseList);

            }
        }
        return null;
    }

    @Override
    public List<OrganizationMenu> adminListByOrgId(Serializable id) {
        return baseMapper.adminListByOrgId(id);
    }

}

