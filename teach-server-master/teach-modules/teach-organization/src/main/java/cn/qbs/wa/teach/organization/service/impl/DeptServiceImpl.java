package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.organization.entity.Dept;
import cn.qbs.wa.teach.organization.entity.EmployeeDept;
import cn.qbs.wa.teach.organization.entity.StudentDept;
import cn.qbs.wa.teach.organization.mapper.DeptMapper;
import cn.qbs.wa.teach.organization.mapper.StudentDeptMapper;
import cn.qbs.wa.teach.organization.pojo.dept.*;
import cn.qbs.wa.teach.organization.pojo.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import cn.qbs.wa.teach.organization.service.DeptService;
import cn.qbs.wa.teach.organization.service.EmployeeDeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门表(Dept)表服务实现类
 *
 * @author makejava
 * @since 2021-11-10 09:54:50
 */
@Slf4j
@Service("deptService")
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Autowired
    EmployeeDeptService employeeDeptService;
    @Autowired
    private StudentDeptMapper studentDeptMapper;

    @Override
    public boolean add(DeptAddRequest params) {
        String name = params.getDeptName().trim();
        Long parentId = params.getParentId();
        if (parentId == null || parentId < 0) {
            parentId = 0L;
        }
        Long count = this.lambdaQuery()
                .eq(Dept::getDeptName, name)
                .eq(Dept::getParentId, parentId)
                .count();
        if (count > 0) {
            throw new ServiceException("已存在同名组织！");
        }

        Dept dept = new Dept();
        BeanUtils.copyProperties(params, dept);

        if (parentId == 0) {
            dept.setParentCode("0");
            dept.setFullName(params.getDeptName());
        } else {
            Dept parent = this.getById(parentId);
            if (parent == null) {
                throw new ServiceException("父组织不存在！");
            }

            dept.setParentCode(parent.getParentCode() + Constants.CATEGORY_PARENT_CODE_SEPARATOR + parentId);
            /*if (dept.getParentCode().split(Constants.CATEGORY_PARENT_CODE_SEPARATOR).length > deptMaxLevel) {
                throw new ServiceException("层级不能超过" + deptMaxLevel + "级！");
            }*/

            dept.setFullName(parent.getFullName() + Constants.CATEGORY_FULL_NAME_SEPARATOR + params.getDeptName());
        }

        if (params.getSort() == null) {
            dept.setSort(selectMaxSortNum(parentId) + 1);
        }

        return this.save(dept);
    }

    /**
     * 查询当前分类下的分类的最大排序号
     * @param parentId
     * @return
     */
    private int selectMaxSortNum(Long parentId) {
        Dept dept = this.getBaseMapper().selectOne(new QueryWrapper<Dept>().eq("parent_id", parentId).select("IFNULL(max(sort),0) as sort"));
        return dept.getSort();
    }

    @Override
    public IPage<DeptPageResponse> page(DeptPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public DeptDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(DeptUpdateRequest params) {
        Long deptId = params.getId();
        if (deptId == null) {
            throw new IllegalParamsException("ID不能为空！");
        }

        Dept dept = this.getById(deptId);
        if (dept == null) {
            throw new IllegalParamsException("分类不存在！");
        }

        String beforeName = dept.getDeptName();
        Long beforeParentId = dept.getParentId();
        String name = params.getDeptName().trim();
        Long parentId = params.getParentId();
        if (parentId == null || parentId < 0) {
            parentId = 0L;
        }
        Long count = this.lambdaQuery()
                .eq(Dept::getDeptName, name)
                .eq(Dept::getParentId, parentId)
                .ne(Dept::getId, deptId)
                .count();
        if (count > 0) {
            throw new ServiceException("已存在同名分类！");
        }

        if (parentId > 0) {
            Dept parent = this.getById(parentId);
            if (parent == null) {
                throw new ServiceException("父节点不存在！");
            }

            dept.setParentCode(parent.getParentCode() + Constants.CATEGORY_PARENT_CODE_SEPARATOR + parentId);
            /*if (dept.getParentCode().split(Constants.CATEGORY_PARENT_CODE_SEPARATOR).length > deptMaxLevel) {
                throw new ServiceException("层级不能超过" + deptMaxLevel + "级！");
            }*/

            dept.setFullName(parent.getFullName() + Constants.CATEGORY_FULL_NAME_SEPARATOR + params.getDeptName());
        } else {
            dept.setParentCode("0");
            dept.setFullName(params.getDeptName());
        }

        dept.setDeptName(params.getDeptName());
        dept.setParentId(parentId);
        dept.setEnabled(params.getEnabled());
        dept.setSort(params.getSort());

        if (dept.getSort() == null) {
            dept.setSort(selectMaxSortNum(parentId) + 1);
        }

        boolean success = this.updateById(dept);
        // 修改了父节点或者分类名称，则同步更新子类
        if (!parentId.equals(beforeParentId) || !dept.getDeptName().equals(beforeName)) {
            this.updateChildren(dept);
        }

        return success;
    }

    private void updateChildren(Dept dept) {
        if (dept != null) {
            Long parentId = dept.getId();
            List<Dept> children = this.lambdaQuery().eq(Dept::getParentId, parentId).list();
            if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(children)) {
                for (Dept child : children) {
                    String parentCode = dept.getParentCode() + Constants.CATEGORY_PARENT_CODE_SEPARATOR + parentId;
                    String fullName = dept.getFullName() + Constants.CATEGORY_FULL_NAME_SEPARATOR + child.getDeptName();
                    this.lambdaUpdate().eq(Dept::getId, child.getId())
                            .set(Dept::getParentCode, parentCode)
                            .set(Dept::getFullName, fullName)
                            .update();

                    child.setParentCode(parentCode);
                    child.setFullName(fullName);
                    this.updateChildren(child);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> idList) {
        if(CollectionUtils.isNotEmpty(idList)){
            for (Long deptId : idList) {
                List<EmployeeDept> existList = employeeDeptService.list(new LambdaQueryWrapper<EmployeeDept>().eq(EmployeeDept::getDeptId, deptId));
                if(CollectionUtils.isNotEmpty(existList)){
                    Dept dept = getById(deptId);
                    throw new ServiceException(dept.getDeptName()+"存在用户,无法删除!");
                }
                List<Dept> childrenList = list(new LambdaQueryWrapper<Dept>().eq(Dept::getParentId, deptId));
                if(CollectionUtils.isNotEmpty(childrenList)){
                    Dept dept = getById(deptId);
                    throw new ServiceException(dept.getDeptName()+"存在子级,无法删除!");
                }
                List<StudentDept> studentDepts = studentDeptMapper.selectList(Wrappers.<StudentDept>lambdaQuery().select(StudentDept::getId).eq(StudentDept::getDeptId, deptId));
                if (CollectionUtils.isNotEmpty(studentDepts)) {
                    Dept dept = getById(deptId);
                    throw new ServiceException(dept.getDeptName()+"存在学员,无法删除!");
                }
            }
        }
        return this.removeByIds(idList);
    }

    @Override
    public TreeDeptTotalResponse treeListByOrgId(Long orgId, DeptListRequest deptListRequest) {
        List<DeptListResponse> deptList = this.baseMapper.listDept(orgId, deptListRequest);
        if (CollectionUtils.isNotEmpty(deptList)) {
            List<TreeDeptResponse> treeDeptResponses = BeanUtil.copyToList(deptList, TreeDeptResponse.class);
            TreeDeptTotalResponse treeDeptTotalResponse = new TreeDeptTotalResponse();
            List<TreeDeptResponse> treeDeptResponseList = (List<TreeDeptResponse>) TreeUtil.tree(treeDeptResponses);
            treeDeptResponseList.stream().forEach(i-> {
                addPeopleCountTotal(i, i.getChildren());
                addStudentCountTotal(i, i.getChildren());
            });
            treeDeptTotalResponse.setDeptList(treeDeptResponseList);
            return treeDeptTotalResponse;
        }
        return null;
    }

    private void addPeopleCountTotal(TreeDeptResponse parent, List<TreeDeptResponse> children) {
        if (CollectionUtils.isNotEmpty(children)) {
            for (TreeDeptResponse child : children) {
                addPeopleCountTotal(child, child.getChildren());
                parent.setPeopleCountTotal(parent.getPeopleCountTotal() + child.getPeopleCountTotal());
            }
        }
    }

    private void addStudentCountTotal(TreeDeptResponse parent, List<TreeDeptResponse> children) {
        if (CollectionUtils.isNotEmpty(children)) {
            for (TreeDeptResponse child : children) {
                addStudentCountTotal(child, child.getChildren());
                parent.setStudentCountTotal(parent.getStudentCountTotal() + child.getStudentCountTotal());
            }
        }
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<Dept> deptList = new ArrayList<>();
        for (Long roleId : idList) {
            Dept dept = new Dept();
            dept.setId(roleId);
            dept.setEnabled(flag);
            deptList.add(dept);
        }
        updateBatchById(deptList);
    }

    @Override
    public List<DeptListResponse> childList(DeptChildRequest params) {
        //List<Dept> deptList = list(new LambdaQueryWrapper<Dept>().eq(Dept::getParentId, params.getParentId()).orderByAsc(Dept::getSort));
        //return BeanUtil.copyToList(deptList, DeptListResponse.class);
        List<DeptListResponse> deptListResponses = this.baseMapper.selectDeptList(params);
        return deptListResponses;
    }

    @Override
    @Async
    public void asyncUpdatePeopleCount(List<Long> idList, Long orgId) {
        if (CollectionUtils.isNotEmpty(idList)) {
            List<Dept> deptList = new ArrayList<>();
            idList=idList.stream().distinct().collect(Collectors.toList());
            for (Long id : idList) {
                Integer peopleCount = this.baseMapper.getPeopleCountById(id);
                Dept dept = new Dept();
                dept.setId(id);
                dept.setPeopleCount(peopleCount);
                deptList.add(dept);
                this.baseMapper.updateDeptCount(dept);
            }

        }

    }


    @Override
    public List<OrgDeptOrRoleResponseDTO> getOrgDept(Serializable id) {
        return this.baseMapper.getOrgDept(id);
    }

    @Override
    public List<DeptDetailResponse> detailList(List<Long> idList) {
        ArrayList<DeptDetailResponse> list = new ArrayList<>();
        List<Dept> deptList = baseMapper.selectBatchIds(idList);
        if (deptList.isEmpty()) {
            return list;
        }
        return deptList.stream().map(e -> BeanUtil.copyProperties(e, DeptDetailResponse.class)).collect(Collectors.toList());
    }

    @Override
    public Long getDeptEmployeeCount(Long orgId, List<Long> deptIdList) {
        return getBaseMapper().selectDeptEmployeeCount(orgId, deptIdList);
    }

    @Override
    public Long getDeptStudentCount(Long orgId, List<Long> deptIdList) {
        return getBaseMapper().selectDeptStudentCount(orgId, deptIdList);
    }

}

