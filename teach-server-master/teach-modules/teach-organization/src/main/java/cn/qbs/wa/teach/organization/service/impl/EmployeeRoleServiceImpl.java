package cn.qbs.wa.teach.organization.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.organization.entity.OrganizationRole;
import cn.qbs.wa.teach.organization.mapper.EmployeeRoleMapper;
import cn.qbs.wa.teach.organization.entity.EmployeeRole;
import cn.qbs.wa.teach.organization.service.EmployeeRoleService;
import cn.qbs.wa.teach.organization.pojo.employeerole.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【职工角色关联关系】(EmployeeRole)表服务实现类
 *
 * @author makejava
 * @since 2021-11-09 20:12:51
 */
@Slf4j
@Service("employeeRoleService")
public class EmployeeRoleServiceImpl extends ServiceImpl<EmployeeRoleMapper, EmployeeRole> implements EmployeeRoleService {

    @Override
    public boolean add(EmployeeRoleAddRequest params) {
        EmployeeRole employeeRole = new EmployeeRole();
        BeanUtils.copyProperties(params, employeeRole);
        return this.save(employeeRole);
    }

    @Override
    public IPage<EmployeeRolePageResponse> page(EmployeeRolePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public EmployeeRoleDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(EmployeeRoleUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        EmployeeRole employeeRole = new EmployeeRole();
        BeanUtils.copyProperties(params, employeeRole);
        return this.updateById(employeeRole);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public void deleteByEmployeeIdAndRoleId(Long employeeId, Long roleId) {
        this.baseMapper.deleteByEmployeeIdAndRoleId(employeeId, roleId);
    }

    @Override
    public List<OrganizationRole> getRole(Long employeeId) {
        return this.baseMapper.getRole(employeeId);
    }
}

