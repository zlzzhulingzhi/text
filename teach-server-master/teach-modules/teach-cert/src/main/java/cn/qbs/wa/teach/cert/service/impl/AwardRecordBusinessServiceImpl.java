package cn.qbs.wa.teach.cert.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.cert.mapper.AwardRecordBusinessMapper;
import cn.qbs.wa.teach.cert.entity.AwardRecordBusiness;
import cn.qbs.wa.teach.cert.service.AwardRecordBusinessService;
import cn.qbs.wa.teach.cert.pojo.awardrecordbusiness.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 基础直播业务关联表(AwardRecordBusiness)表服务实现类
 *
 * @author makejava
 * @since 2022-01-19 11:38:18
 */
@Slf4j
@Service("awardRecordBusinessService")
public class AwardRecordBusinessServiceImpl extends ServiceImpl<AwardRecordBusinessMapper, AwardRecordBusiness> implements AwardRecordBusinessService {

    @Override
    public boolean add(AwardRecordBusinessAddRequest params) {
        AwardRecordBusiness awardRecordBusiness = new AwardRecordBusiness();
        BeanUtils.copyProperties(params, awardRecordBusiness);
        return this.save(awardRecordBusiness);
    }

    @Override
    public IPage<AwardRecordBusinessPageResponse> page(AwardRecordBusinessPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public AwardRecordBusinessDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(AwardRecordBusinessUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        AwardRecordBusiness awardRecordBusiness = new AwardRecordBusiness();
        BeanUtils.copyProperties(params, awardRecordBusiness);
        return this.updateById(awardRecordBusiness);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

