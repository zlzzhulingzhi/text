package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.EmployeeInfo;
import cn.qbs.wa.train.logistics.pojo.employeeinfo.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 职工信息(EmployeeInfo)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-01-21 11:30:04
 */
public interface EmployeeInfoService extends IService<EmployeeInfo> {

    /**
     * 新增职工信息
     * @param params
     * @return
     */
    boolean add(EmployeeInfoAddRequest params);

    /**
     * 分页查询职工信息
     * @param params
     * @return
     */
    IPage<EmployeeInfoPageResponse> page(EmployeeInfoPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    EmployeeInfoDetailResponse detail(Serializable id);

    /**
     * 更新职工信息
     * @param params
     * @return
     */
    boolean update(EmployeeInfoUpdateRequest params);

    /**
     * 删除职工信息
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);
    
}

