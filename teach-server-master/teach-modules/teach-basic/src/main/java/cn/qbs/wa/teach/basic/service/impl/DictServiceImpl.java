package cn.qbs.wa.teach.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.basic.entity.Dict;
import cn.qbs.wa.teach.basic.mapper.DictMapper;
import cn.qbs.wa.teach.basic.pojo.dict.*;
import cn.qbs.wa.teach.basic.service.DictService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典表(Dict)表服务实现类
 *
 * @author makejava
 * @since 2021-11-08 13:32:05
 */
@Slf4j
@Service("dictService")
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(DictAddRequest params) {
        if(params.getDictKey()==null){
            params.setDictKey("-1");
        }
        if(params.getParentId()==null){
            List<Dict> existList = list(new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, Constants.DB_FAIL).eq(Dict::getCode, params.getCode()));
            if(CollectionUtils.isNotEmpty(existList)){
                throw new ServiceException("已存在重复的code");
            }
        }
        Dict dict = new Dict();
        BeanUtils.copyProperties(params, dict);
        Boolean save=this.save(dict);
        List<Dict> existList = list(new LambdaQueryWrapper<Dict>().eq(Dict::getDictKey, dict.getDictKey()).eq(Dict::getCode, dict.getCode()).ne(Dict::getId,dict.getId()));
        if(CollectionUtils.isNotEmpty(existList)){
            throw new ServiceException("已存在重复的key");
        }
        return save;
    }

    @Override
    public IPage<DictPageResponse> page(DictPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public String getDictKey(DictPageRequest params) {
        return baseMapper.getDictKey(params);
    }

    @Override
    public String getDictValue(DictPageRequest params) {
        return baseMapper.getDictValue(params);
    }

    @Override
    public DictDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(DictUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        List<Dict> existList = list(new LambdaQueryWrapper<Dict>().eq(Dict::getDictKey, params.getDictKey()).eq(Dict::getCode, params.getCode()).ne(Dict::getId,params.getId()));
        if(CollectionUtils.isNotEmpty(existList)){
            throw new ServiceException("已存在重复的key");
        }
        Dict dict = new Dict();
        BeanUtils.copyProperties(params, dict);
        return this.updateById(dict);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<TreeDictResponse> treeDict(TreeDictRequest params) {
        List<Dict> dictList = list(new LambdaQueryWrapper<Dict>().eq(Dict::getCode, params.getCode()).orderByAsc(Dict::getSort));
        if (CollectionUtils.isNotEmpty(dictList)) {
            List<TreeDictResponse> treeDictResponses = new ArrayList<>();
            for (Dict dict : dictList) {
                TreeDictResponse treeDictResponse = new TreeDictResponse();
                BeanUtils.copyProperties(dict, treeDictResponse);
                treeDictResponses.add(treeDictResponse);
            }
            return (List<TreeDictResponse>) TreeUtil.tree(treeDictResponses);
        }
        return null;
    }

    @Override
    public List<DictListResponse> childList(DictChildRequest params) {
        List<Dict> dictList = list(new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, params.getParentId()).orderByAsc(Dict::getSort));
        return BeanUtil.copyToList(dictList, DictListResponse.class);
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<Dict> dictList = new ArrayList<>();
        for (Long roleId : idList) {
            Dict dict = new Dict();
            dict.setId(roleId);
            dict.setEnabled(flag);
            dictList.add(dict);
        }
        updateBatchById(dictList);
    }


}

