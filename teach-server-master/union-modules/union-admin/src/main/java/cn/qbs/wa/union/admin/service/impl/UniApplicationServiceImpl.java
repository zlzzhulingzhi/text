package cn.qbs.wa.union.admin.service.impl;

import cn.qbs.wa.union.admin.entity.UniApplication;
import cn.qbs.wa.union.admin.mapper.UniApplicationMapper;
import cn.qbs.wa.union.admin.pojo.uniapplication.*;
import cn.qbs.wa.union.admin.service.UniApplicationService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 统一应用展示(UniApplication)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
@Slf4j
@Service("uniApplicationService")
public class UniApplicationServiceImpl extends ServiceImpl<UniApplicationMapper, UniApplication> implements UniApplicationService {

    @Override
    public boolean add(UniApplicationAddRequest params) {
        UniApplication uniApplication = new UniApplication();
        BeanUtils.copyProperties(params, uniApplication);
        return this.save(uniApplication);
    }

    @Override
    public IPage<UniApplicationPageResponse> page(UniApplicationPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public UniApplicationDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(UniApplicationUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        UniApplication uniApplication = new UniApplication();
        BeanUtils.copyProperties(params, uniApplication);
        return this.updateById(uniApplication);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

