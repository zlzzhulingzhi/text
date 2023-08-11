package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.EmployeeRole;
import cn.qbs.wa.train.logistics.entity.OrganizationRole;
import cn.qbs.wa.train.logistics.pojo.employeerole.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【职工角色关联关系】(EmployeeRole)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:12:51
 */
public interface EmployeeRoleService extends IService<EmployeeRole> {

    /**
     * 新增【职工角色关联关系】
     * @param params
     * @return
     */
    boolean add(EmployeeRoleAddRequest params);

    /**
     * 分页查询【职工角色关联关系】
     * @param params
     * @return
     */
    IPage<EmployeeRolePageResponse> page(EmployeeRolePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    EmployeeRoleDetailResponse detail(Serializable id);

    /**
     * 更新【职工角色关联关系】
     * @param params
     * @return
     */
    boolean update(EmployeeRoleUpdateRequest params);

    /**
     * 删除【职工角色关联关系】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 删除【职工角色关联关系根据职工id和角色id】
     * @param employeeId
     * @return
     */
    void deleteByEmployeeIdAndRoleId(Long employeeId, Long roleId);

    List<OrganizationRole> getRole(Long employeeId);
}

