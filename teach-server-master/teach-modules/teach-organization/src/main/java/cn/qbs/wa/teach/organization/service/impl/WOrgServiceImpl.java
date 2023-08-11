package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.organization.entity.WOrg;
import cn.qbs.wa.teach.organization.mapper.WOrgMapper;
import cn.qbs.wa.teach.organization.pojo.worg.*;
import cn.qbs.wa.teach.organization.service.WOrgService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 机构插件表(WOrg)表服务实现类
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
@Slf4j
@Service("wOrgService")
public class WOrgServiceImpl extends ServiceImpl<WOrgMapper, WOrg> implements WOrgService {

    @Override
    public boolean add(WOrgAddRequest params) {
        List<Long> idList = params.getIdList();
        List<WOrg> wOrgs = listByIds(idList);
        if(CollUtil.isNotEmpty(wOrgs)){
            idList.removeAll(wOrgs.stream().map(WOrg::getId).collect(Collectors.toList()));
        }
        if (CollUtil.isNotEmpty(idList)) {
            List<WOrg> wOrgList = idList.stream().map(i -> {
                WOrg wOrg = new WOrg();
                wOrg.setId(i);
                return wOrg;
            }).collect(Collectors.toList());
            saveBatch(wOrgList);
        }
        return true;
    }

    @Override
    public IPage<WOrgPageResponse> page(WOrgPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public WOrgDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(WOrgUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        WOrg wOrg = new WOrg();
        BeanUtils.copyProperties(params, wOrg);
        return this.updateById(wOrg);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

