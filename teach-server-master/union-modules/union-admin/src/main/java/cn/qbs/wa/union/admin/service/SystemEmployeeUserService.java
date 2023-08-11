package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.SystemEmployeeUser;
import cn.qbs.wa.union.admin.pojo.systememployeeuser.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 职工用户表(SystemEmployeeUser)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:03
 */
public interface SystemEmployeeUserService extends IService<SystemEmployeeUser> {

    /**
     * 新增职工用户表
     *
     * @param params
     * @return
     */
    SystemEmployeeUser add(SystemEmployeeUserAddRequest params);

    /**
     * 分页查询职工用户表
     *
     * @param params
     * @return
     */
    IPage<SystemEmployeeUserPageResponse> page(SystemEmployeeUserPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    SystemEmployeeUserDetailResponse detail(Serializable id);

    /**
     * 更新职工用户表
     *
     * @param params
     * @return
     */
    boolean update(SystemEmployeeUserUpdateRequest params);

    /**
     * 删除职工用户表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    SystemEmployeeUser innerAdd(InnerEmployeeUserAddRequest params);

    void innerUpdate(InnerEmployeeUserUpdateRequest params);
}

