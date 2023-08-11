package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.exam.admin.mapper.TCertRuleMapper;
import cn.qbs.wa.teach.exam.common.entity.TCertRule;
import cn.qbs.wa.teach.exam.admin.service.TCertRuleService;
import cn.qbs.wa.teach.exam.admin.pojo.tcertrule.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 证书规则表(TCertRule)表服务实现类
 *
 * @author makejava
 * @since 2022-05-16 13:49:21
 */
@Slf4j
@Service("tCertRuleService")
public class TCertRuleServiceImpl extends ServiceImpl<TCertRuleMapper, TCertRule> implements TCertRuleService {

    @Override
    public boolean add(TCertRuleAddRequest params) {
        TCertRule tCertRule = new TCertRule();
        BeanUtils.copyProperties(params, tCertRule);
        return this.save(tCertRule);
    }

    @Override
    public IPage<TCertRulePageResponse> page(TCertRulePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public TCertRuleDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(TCertRuleUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        TCertRule tCertRule = new TCertRule();
        BeanUtils.copyProperties(params, tCertRule);
        return this.updateById(tCertRule);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }
    
}

