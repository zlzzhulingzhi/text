package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.standard.mapper.CourseStudentStudyRecordMapper;
import cn.qbs.wa.teach.course.common.entity.CourseStudentStudyRecord;
import cn.qbs.wa.teach.course.standard.service.CourseStudentStudyRecordService;
import cn.qbs.wa.teach.course.standard.pojo.coursestudentstudyrecord.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程学习记录】(CourseStudentStudyRecord)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Slf4j
@Service("courseStudentStudyRecordService")
public class CourseStudentStudyRecordServiceImpl extends ServiceImpl<CourseStudentStudyRecordMapper, CourseStudentStudyRecord> implements CourseStudentStudyRecordService {

    @Override
    public boolean add(CourseStudentStudyRecordAddRequest params) {
        CourseStudentStudyRecord courseStudentStudyRecord = new CourseStudentStudyRecord();
        BeanUtils.copyProperties(params, courseStudentStudyRecord);
        return this.save(courseStudentStudyRecord);
    }

    @Override
    public IPage<CourseStudentStudyRecordPageResponse> page(CourseStudentStudyRecordPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseStudentStudyRecordDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseStudentStudyRecordUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseStudentStudyRecord courseStudentStudyRecord = new CourseStudentStudyRecord();
        BeanUtils.copyProperties(params, courseStudentStudyRecord);
        return this.updateById(courseStudentStudyRecord);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

