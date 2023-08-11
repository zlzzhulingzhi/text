package cn.qbs.wa.train.logistics.service.impl;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;
import cn.qbs.wa.train.logistics.mapper.ClazzLessonArrangeMapper;
import cn.qbs.wa.train.logistics.mapper.ClazzLessonMapper;
import cn.qbs.wa.train.logistics.entity.ClazzLesson;
import cn.qbs.wa.train.logistics.pojo.clazzlessonarrange.ClazzLessonArrangeAddRequest;
import cn.qbs.wa.train.logistics.service.ClazzLessonService;
import cn.qbs.wa.train.logistics.pojo.clazzlesson.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 班级课程(ClazzLesson)表服务实现类
 *
 * @author makejava
 * @since 2023-03-13 20:12:56
 */
@Slf4j
@Service("clazzLessonService")
public class ClazzLessonServiceImpl extends ServiceImpl<ClazzLessonMapper, ClazzLesson> implements ClazzLessonService {

    @Resource
    ClazzLessonArrangeMapper clazzLessonArrangeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addBatch(List<ClazzLessonAddRequest> params) {
        for (ClazzLessonAddRequest param:params) {
            param.setOrgId(SecurityContextHolder.getOrgId());
            ClazzLesson clazzLesson = new ClazzLesson();
            BeanUtils.copyProperties(param, clazzLesson);
            this.save(clazzLesson);
            if(CollectionUtils.isNotEmpty(param.getClazzLessonArrangeList())){
                for (ClazzLessonArrange clazzLessonArrange:param.getClazzLessonArrangeList()) {
                    clazzLessonArrange.setOrgId(SecurityContextHolder.getOrgId());
                    clazzLessonArrange.setLessonId(clazzLesson.getId());
                }
                clazzLessonArrangeMapper.insertBatch(param.getClazzLessonArrangeList());
            }
        }
        return true;
    }

    @Override
    public boolean add(ClazzLessonAddRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        ClazzLesson clazzLesson = new ClazzLesson();
        BeanUtils.copyProperties(params, clazzLesson);
        return this.save(clazzLesson);
    }

    @Override
    public IPage<ClazzLessonPageResponse> page(ClazzLessonPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ClazzLessonDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ClazzLessonUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ClazzLesson clazzLesson = new ClazzLesson();
        BeanUtils.copyProperties(params, clazzLesson);
        return this.updateById(clazzLesson);
    }

    @Override
    public boolean sort(ClazzLessonUpdateRequest params) {
        if(CollectionUtils.isNotEmpty(params.getSortedIds())){
            for (int i=0;i<params.getSortedIds().size();i++){
                ClazzLesson clazzLesson=new ClazzLesson();
                clazzLesson.setSort(i+1);
                clazzLesson.setId(params.getSortedIds().get(i));
                this.updateById(clazzLesson);
            }
        }
        return true;
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        Long count=clazzLessonArrangeMapper.selectCount(new LambdaQueryWrapper<ClazzLessonArrange>().
                in(ClazzLessonArrange::getLessonId,idList).eq(ClazzLessonArrange::getOrgId,SecurityContextHolder.getOrgId()));
        if(count>0){
            throw new ServiceException("有课程安排无法删除") ;
        }
        return this.removeByIds(idList);
    }

}

