package cn.qbs.wa.teach.basic.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.basic.mapper.ApplicationApplicationTypeMapper;
import cn.qbs.wa.teach.basic.entity.ApplicationApplicationType;
import cn.qbs.wa.teach.basic.service.ApplicationApplicationTypeService;
import cn.qbs.wa.teach.basic.pojo.applicationapplicationtype.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * (ApplicationApplicationType)表服务实现类
 *
 * @author makejava
 * @since 2021-11-10 10:22:56
 */
@Slf4j
@Service("applicationApplicationTypeService")
public class ApplicationApplicationTypeServiceImpl extends ServiceImpl<ApplicationApplicationTypeMapper, ApplicationApplicationType> implements ApplicationApplicationTypeService {

    @Override
    public boolean add(ApplicationApplicationTypeAddRequest params) {
        ApplicationApplicationType applicationApplicationType = new ApplicationApplicationType();
        BeanUtils.copyProperties(params, applicationApplicationType);
        return this.save(applicationApplicationType);
    }

    @Override
    public IPage<ApplicationApplicationTypePageResponse> page(ApplicationApplicationTypePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ApplicationApplicationTypeDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ApplicationApplicationTypeUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ApplicationApplicationType applicationApplicationType = new ApplicationApplicationType();
        BeanUtils.copyProperties(params, applicationApplicationType);
        return this.updateById(applicationApplicationType);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

