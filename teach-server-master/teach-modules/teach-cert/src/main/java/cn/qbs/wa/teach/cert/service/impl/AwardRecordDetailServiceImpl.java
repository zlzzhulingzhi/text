package cn.qbs.wa.teach.cert.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.cert.mapper.AwardRecordDetailMapper;
import cn.qbs.wa.teach.cert.entity.AwardRecordDetail;
import cn.qbs.wa.teach.cert.service.AwardRecordDetailService;
import cn.qbs.wa.teach.cert.pojo.awardrecorddetail.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 证书配置(AwardRecordDetail)表服务实现类
 *
 * @author makejava
 * @since 2022-01-19 11:38:19
 */
@Slf4j
@Service("awardRecordDetailService")
public class AwardRecordDetailServiceImpl extends ServiceImpl<AwardRecordDetailMapper, AwardRecordDetail> implements AwardRecordDetailService {

    @Override
    public boolean add(AwardRecordDetailAddRequest params) {
        AwardRecordDetail awardRecordDetail = new AwardRecordDetail();
        BeanUtils.copyProperties(params, awardRecordDetail);
        return this.save(awardRecordDetail);
    }

    @Override
    public IPage<AwardRecordDetailPageResponse> page(AwardRecordDetailPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public AwardRecordDetailDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(AwardRecordDetailUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        AwardRecordDetail awardRecordDetail = new AwardRecordDetail();
        BeanUtils.copyProperties(params, awardRecordDetail);
        return this.updateById(awardRecordDetail);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

