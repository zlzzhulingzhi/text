package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.train.logistics.entity.Groups;
import cn.qbs.wa.train.logistics.mapper.GroupsMapper;
import cn.qbs.wa.train.logistics.pojo.groups.*;
import cn.qbs.wa.train.logistics.service.GroupsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分组表(Groups)表服务实现类
 *
 * @author makejava
 * @since 2022-03-28 09:27:33
 */
@Slf4j
@Service("groupsService")
public class GroupsServiceImpl extends ServiceImpl<GroupsMapper, Groups> implements GroupsService {

    @Override
    public boolean add(GroupsAddRequest params) {
        Groups groups = new Groups();
        BeanUtils.copyProperties(params, groups);
        return this.save(groups);
    }

    @Override
    public IPage<GroupsPageResponse> page(GroupsPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public GroupsDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(GroupsUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Groups groups = new Groups();
        BeanUtils.copyProperties(params, groups);
        return this.updateById(groups);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<GroupsPageResponse> selectAll() {
        return baseMapper.selectAll();
    }

    @Override
    public List<GroupsDetailResponse> detailList(List<Long> idList) {
        return BeanUtil.copyToList(listByIds(idList),GroupsDetailResponse.class);
    }

    @Override
    public List<GroupsDetailResponse> selectListAll() {
        return baseMapper.selectListAll();
    }

    @Override
    public List<GroupsTreeResponse> tree(GroupsTreeRequest params) {
        List<Groups> groupsList = this.lambdaQuery().orderByAsc(Groups::getSort).list();
        if (CollectionUtil.isNotEmpty(groupsList)){
            List<GroupsTreeResponse> groupsTreeList = new ArrayList<>();
            for (Groups groups : groupsList){
                GroupsTreeResponse groupsTree = new GroupsTreeResponse();
                BeanUtils.copyProperties(groups,groupsTree);
                groupsTreeList.add(groupsTree);
            }
            return TreeUtil.tree(groupsTreeList);
        }
        return null;
    }
}

