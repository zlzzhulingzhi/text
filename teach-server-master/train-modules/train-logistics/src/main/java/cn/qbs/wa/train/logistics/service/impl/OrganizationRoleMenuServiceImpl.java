package cn.qbs.wa.train.logistics.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.logistics.entity.OrganizationRoleMenu;
import cn.qbs.wa.train.logistics.mapper.OrganizationRoleMenuMapper;
import cn.qbs.wa.train.logistics.pojo.organizationrolemenu.*;
import cn.qbs.wa.train.logistics.service.OrganizationRoleMenuService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【角色菜单关联关系】(OrganizationRoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2021-11-12 08:58:32
 */
@Slf4j
@Service("organizationRoleMenuService")
public class OrganizationRoleMenuServiceImpl extends ServiceImpl<OrganizationRoleMenuMapper, OrganizationRoleMenu> implements OrganizationRoleMenuService {

    @Override
    public boolean add(OrganizationRoleMenuAddRequest params) {
        OrganizationRoleMenu organizationRoleMenu = new OrganizationRoleMenu();
        BeanUtils.copyProperties(params, organizationRoleMenu);
        return this.save(organizationRoleMenu);
    }

    @Override
    public IPage<OrganizationRoleMenuPageResponse> page(OrganizationRoleMenuPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public OrganizationRoleMenuDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(OrganizationRoleMenuUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        OrganizationRoleMenu organizationRoleMenu = new OrganizationRoleMenu();
        BeanUtils.copyProperties(params, organizationRoleMenu);
        return this.updateById(organizationRoleMenu);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<OrganizationRoleMenu> listByAdmin(Long orgId) {
        return this.baseMapper.listByAdmin( orgId);
    }

    @Override
    public void deleteByAdminIds(List<Long> idList) {
        this.baseMapper.deleteByAdminIds( idList);
    }

}

