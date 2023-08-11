package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.union.admin.entity.UniApplicationType;
import cn.qbs.wa.union.admin.mapper.UniApplicationTypeMapper;
import cn.qbs.wa.union.admin.pojo.uniapplicationtype.*;
import cn.qbs.wa.union.admin.service.UniApplicationTypeService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 统一应用类型(UniApplicationType)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@Slf4j
@Service("uniApplicationTypeService")
public class UniApplicationTypeServiceImpl extends ServiceImpl<UniApplicationTypeMapper, UniApplicationType> implements UniApplicationTypeService {

    @Override
    public boolean add(UniApplicationTypeAddRequest params) {
        UniApplicationType uniApplicationType = new UniApplicationType();
        BeanUtils.copyProperties(params, uniApplicationType);
        return this.save(uniApplicationType);
    }

    @Override
    public IPage<UniApplicationTypePageResponse> page(UniApplicationTypePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public UniApplicationTypeDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(UniApplicationTypeUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        UniApplicationType uniApplicationType = new UniApplicationType();
        BeanUtils.copyProperties(params, uniApplicationType);
        return this.updateById(uniApplicationType);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<UniApplicationTypeTreeResponse> getTreeList(UniApplicationTypeTreeRequest request) {
        List<UniApplicationType> uniApplicationTypeList = list(new LambdaQueryWrapper<UniApplicationType>().orderByAsc(UniApplicationType::getSort));
        if(CollUtil.isNotEmpty(uniApplicationTypeList)){
            return TreeUtil.tree(BeanUtil.copyToList(uniApplicationTypeList,UniApplicationTypeTreeResponse.class));
        }
        return null;
    }

}

