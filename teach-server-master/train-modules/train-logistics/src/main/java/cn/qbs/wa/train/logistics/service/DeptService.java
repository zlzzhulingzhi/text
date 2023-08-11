package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.Dept;
import cn.qbs.wa.train.logistics.pojo.dept.*;
import cn.qbs.wa.train.logistics.pojo.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 部门表(Dept)表服务接口
 *
 * @author makejava
 * @since 2021-11-10 09:54:50
 */
public interface DeptService extends IService<Dept> {

    /**
     * 新增部门表
     * @param params
     * @return
     */
    boolean add(DeptAddRequest params);

    /**
     * 分页查询部门表
     * @param params
     * @return
     */
    IPage<DeptPageResponse> page(DeptPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    DeptDetailResponse detail(Serializable id);

    /**
     * 更新部门表
     * @param params
     * @return
     */
    boolean update(DeptUpdateRequest params);

    /**
     * 删除部门表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    TreeDeptTotalResponse treeListByOrgId(Long id, DeptListRequest deptListRequest);

    void batchEnabled(Integer flag, List<Long> idList);

    List<DeptListResponse> childList(DeptChildRequest params);

    void asyncUpdatePeopleCount(List<Long> idList, Long orgId);

    List<OrgDeptOrRoleResponseDTO> getOrgDept(Serializable id);

    List<DeptDetailResponse> detailList(List<Long> idList);

    Long getDeptEmployeeCount(Long orgId, List<Long> deptIdList);

    Long getDeptStudentCount(Long orgId, List<Long> deptIdList);

}

