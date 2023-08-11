package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.EmployeeDept;
import cn.qbs.wa.train.logistics.pojo.dept.EmployeeDeptListResponse;
import cn.qbs.wa.train.logistics.pojo.employeedept.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 职工部门表(EmployeeDept)表服务接口
 *
 * @author makejava
 * @since 2021-11-09 20:11:35
 */
public interface EmployeeDeptService extends IService<EmployeeDept> {

    /**
     * 新增职工部门表
     * @param params
     * @return
     */
    boolean add(EmployeeDeptAddRequest params);

    /**
     * 分页查询职工部门表
     * @param params
     * @return
     */
    IPage<EmployeeDeptPageResponse> page(EmployeeDeptPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    EmployeeDeptDetailResponse detail(Serializable id);

    /**
     * 更新职工部门表
     * @param params
     * @return
     */
    boolean update(EmployeeDeptUpdateRequest params);

    /**
     * 删除职工部门表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<Long> listIdByEmployeeId(Serializable id);


    /**
     * 查询职工部门
     * @param params 查询参数
     */
    List<EmployeeDeptTreeListResponse> listTree(EmployeeDeptListRequest params);


    List<EmployeeDeptListResponse> list(EmployeeDeptListRequest params);
}

