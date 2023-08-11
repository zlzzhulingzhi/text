package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.standard.mapper.TActivityMapper;
import cn.qbs.wa.teach.course.standard.entity.TActivity;
import cn.qbs.wa.teach.course.standard.service.TActivityService;
import cn.qbs.wa.teach.course.standard.pojo.tactivity.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动表(TActivity)表服务实现类
 *
 * @author makejava
 * @since 2022-12-13 15:55:03
 */
@Slf4j
@Service("tActivityService")
public class TActivityServiceImpl extends ServiceImpl<TActivityMapper, TActivity> implements TActivityService {

    @Override
    public boolean add(TActivityAddRequest params) {
        TActivity tActivity = new TActivity();
        BeanUtils.copyProperties(params, tActivity);
        return this.save(tActivity);
    }

    @Override
    public IPage<TActivityPageResponse> page(TActivityPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public TActivityDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(TActivityUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        if(Constants.DB_TRUE.equals(params.getShelfStatus())){
            params.setShelfTime(LocalDateTime.now());
        }
        TActivity tActivity = new TActivity();
        BeanUtils.copyProperties(params, tActivity);
        return this.updateById(tActivity);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        for (Long id:idList) {
            TActivity tActivity=baseMapper.selectById(id);
            if(tActivity!=null && Constants.DB_TRUE.equals(tActivity.getShelfStatus())){
                throw new IllegalParamsException("请先下架需要删除的活动！");
            }
        }
        return this.removeByIds(idList);
    }

    @Override
    public boolean updateShelfStatus(TActivityUpdateShelfStatusRequest params) {
        if (params.getIds() == null || params.getIds().isEmpty()) {
            throw new IllegalParamsException("ID不能为空！");
        }
        if(Constants.DB_TRUE.equals(params.getShelfStatus())){
            params.setShelfTime(LocalDateTime.now());
        }
        List<TActivity> tActivities=new ArrayList<>();
        for (Long id:params.getIds()) {
            TActivity tActivity = new TActivity();
            BeanUtils.copyProperties(params, tActivity);
            tActivity.setId(id);
            tActivities.add(tActivity);
        }
        return this.updateBatchById(tActivities);
    }

}

