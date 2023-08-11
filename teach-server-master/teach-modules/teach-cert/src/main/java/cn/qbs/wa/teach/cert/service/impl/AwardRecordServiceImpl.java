package cn.qbs.wa.teach.cert.service.impl;

import cn.qbs.wa.teach.cert.pojo.cert.PersonCertDetailRequest;
import cn.qbs.wa.teach.cert.pojo.cert.CertDetailResponse;
import cn.qbs.wa.teach.cert.pojo.cert.PersonCertDetailResponse;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.cert.mapper.AwardRecordMapper;
import cn.qbs.wa.teach.cert.entity.AwardRecord;
import cn.qbs.wa.teach.cert.service.AwardRecordService;
import cn.qbs.wa.teach.cert.pojo.awardrecord.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 颁发记录(AwardRecord)表服务实现类
 *
 * @author makejava
 * @since 2022-01-19 11:38:17
 */
@Slf4j
@Service("awardRecordService")
public class AwardRecordServiceImpl extends ServiceImpl<AwardRecordMapper, AwardRecord> implements AwardRecordService {

    @Override
    public boolean add(AwardRecordAddRequest params) {
        AwardRecord awardRecord = new AwardRecord();
        BeanUtils.copyProperties(params, awardRecord);
        return this.save(awardRecord);
    }

    @Override
    public IPage<AwardRecordPageResponse> page(AwardRecordPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public AwardRecordDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(AwardRecordUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        AwardRecord awardRecord = new AwardRecord();
        BeanUtils.copyProperties(params, awardRecord);
        return this.updateById(awardRecord);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<AwardRecordCountResponse> listAwardCount(List<Long> certIdList) {
        return baseMapper.listAwardCount(certIdList);
    }

    @Override
    public IPage<AwardRecordPageResponse> selectConditionPage(AwardRecordPageRequest params) {
        return baseMapper.selectConditionPage(params.createMpPage(), params);
    }

    @Override
    public List<AwardRecord> selectConditionList(AwardRecordListRequest params) {
        return baseMapper.selectConditionList(params);
    }

}

