package cn.qbs.wa.teach.basic.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.basic.mapper.ApplicationTypeMapper;
import cn.qbs.wa.teach.basic.entity.ApplicationType;
import cn.qbs.wa.teach.basic.service.ApplicationTypeService;
import cn.qbs.wa.teach.basic.pojo.applicationtype.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * (ApplicationType)表服务实现类
 *
 * @author makejava
 * @since 2021-11-09 19:14:28
 */
@Slf4j
@Service("applicationTypeService")
public class ApplicationTypeServiceImpl extends ServiceImpl<ApplicationTypeMapper, ApplicationType> implements ApplicationTypeService {

    @Override
    public boolean add(ApplicationTypeAddRequest params) {
        ApplicationType applicationType = new ApplicationType();
        BeanUtils.copyProperties(params, applicationType);
        return this.save(applicationType);
    }

    @Override
    public IPage<ApplicationTypePageResponse> page(ApplicationTypePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ApplicationTypeDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ApplicationTypeUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ApplicationType applicationType = new ApplicationType();
        BeanUtils.copyProperties(params, applicationType);
        return this.updateById(applicationType);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

