package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.TreeNode;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.organization.entity.EmployeeDept;
import cn.qbs.wa.teach.organization.mapper.EmployeeDeptMapper;
import cn.qbs.wa.teach.organization.pojo.dept.EmployeeDeptListResponse;
import cn.qbs.wa.teach.organization.pojo.employeedept.*;
import cn.qbs.wa.teach.organization.service.EmployeeDeptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 职工部门表(EmployeeDept)表服务实现类
 *
 * @author makejava
 * @since 2021-11-09 20:11:35
 */
@Slf4j
@Service("employeeDeptService")
public class EmployeeDeptServiceImpl extends ServiceImpl<EmployeeDeptMapper, EmployeeDept> implements EmployeeDeptService {

    @Override
    public boolean add(EmployeeDeptAddRequest params) {
        EmployeeDept employeeDept = new EmployeeDept();
        BeanUtils.copyProperties(params, employeeDept);
        return this.save(employeeDept);
    }

    @Override
    public IPage<EmployeeDeptPageResponse> page(EmployeeDeptPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public EmployeeDeptDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(EmployeeDeptUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        EmployeeDept employeeDept = new EmployeeDept();
        BeanUtils.copyProperties(params, employeeDept);
        return this.updateById(employeeDept);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<Long> listIdByEmployeeId(Serializable id) {
        return this.baseMapper.listIdByEmployeeId( id);
    }

    @Override
    public List<EmployeeDeptTreeListResponse> listTree(EmployeeDeptListRequest params) {
        List<EmployeeDeptTreeListResponse> list = this.baseMapper.list(params);
        if (list.isEmpty()) {
            return list;
        }
        List<? extends TreeNode> treeList = null;
        if (params.getDeptId() != null) {
            treeList = TreeUtil.getChild(list, params.getDeptId().toString());
        } else {
            treeList = TreeUtil.tree(list);
        }

        return (List<EmployeeDeptTreeListResponse>) treeList;
    }

    @Override
    public List<EmployeeDeptListResponse> list(EmployeeDeptListRequest params) {
        List<EmployeeDeptTreeListResponse> list = this.baseMapper.list(params);
        if(CollUtil.isNotEmpty(list)){
          return  BeanUtil.copyToList(list,EmployeeDeptListResponse.class);
        }
        return null;
    }
}

