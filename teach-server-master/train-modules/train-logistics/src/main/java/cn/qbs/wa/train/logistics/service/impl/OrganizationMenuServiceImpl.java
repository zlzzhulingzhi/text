package cn.qbs.wa.train.logistics.service.impl;


import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.train.basic.api.RemoteTrainMenuService;
import cn.qbs.wa.train.basic.api.pojo.DTO.menu.MenuListDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.menu.MenuListResultDTO;
import cn.qbs.wa.train.logistics.entity.OrganizationMenu;
import cn.qbs.wa.train.logistics.mapper.OrganizationMenuMapper;
import cn.qbs.wa.train.logistics.pojo.organizationmenu.*;
import cn.qbs.wa.train.logistics.service.OrganizationMenuService;
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
    RemoteTrainMenuService remoteMenuService;

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
        // 列出教务系统所有菜单
        R<List<MenuListResultDTO>> menuListResult = remoteMenuService.listAllMenu();
        if (R.FAIL == menuListResult.getCode()) {
            throw new ServiceException(menuListResult.getMsg());
        }
        List<MenuListResultDTO> menuListResultDTO = menuListResult.getData();
        List<OrganizationMenuListResponse> responseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(menuListResultDTO)) {
            for (MenuListResultDTO listResultDTO : menuListResultDTO) {
                OrganizationMenuListResponse organizationMenuListResponse = new OrganizationMenuListResponse();
                BeanUtils.copyProperties(listResultDTO, organizationMenuListResponse);
                responseList.add(organizationMenuListResponse);
            }
            return TreeUtil.tree(responseList);
        }
        return responseList;
    }

    @Override
    public List<OrganizationMenu> adminListByOrgId(Serializable id) {
        return baseMapper.adminListByOrgId(id);
    }

}

