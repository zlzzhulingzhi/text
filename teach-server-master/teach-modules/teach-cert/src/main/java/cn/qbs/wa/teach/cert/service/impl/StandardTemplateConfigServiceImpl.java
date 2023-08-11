package cn.qbs.wa.teach.cert.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.cert.mapper.StandardTemplateConfigMapper;
import cn.qbs.wa.teach.cert.entity.StandardTemplateConfig;
import cn.qbs.wa.teach.cert.service.StandardTemplateConfigService;
import cn.qbs.wa.teach.cert.pojo.standardtemplateconfig.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 证书模板配置(StandardTemplateConfig)表服务实现类
 *
 * @author makejava
 * @since 2022-01-19 11:38:22
 */
@Slf4j
@Service("standardTemplateConfigService")
public class StandardTemplateConfigServiceImpl extends ServiceImpl<StandardTemplateConfigMapper, StandardTemplateConfig> implements StandardTemplateConfigService {

    @Override
    public boolean add(StandardTemplateConfigAddRequest params) {
        StandardTemplateConfig standardTemplateConfig = new StandardTemplateConfig();
        BeanUtils.copyProperties(params, standardTemplateConfig);
        return this.save(standardTemplateConfig);
    }

    @Override
    public IPage<StandardTemplateConfigPageResponse> page(StandardTemplateConfigPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public StandardTemplateConfigDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(StandardTemplateConfigUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        StandardTemplateConfig standardTemplateConfig = new StandardTemplateConfig();
        BeanUtils.copyProperties(params, standardTemplateConfig);
        return this.updateById(standardTemplateConfig);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

