package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.standard.mapper.CourseMessageReplyMapper;
import cn.qbs.wa.teach.course.common.entity.CourseMessageReply;
import cn.qbs.wa.teach.course.standard.service.CourseMessageReplyService;
import cn.qbs.wa.teach.course.standard.pojo.coursemessagereply.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程留言回复记录】(CourseMessageReply)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Slf4j
@Service("courseMessageReplyService")
public class CourseMessageReplyServiceImpl extends ServiceImpl<CourseMessageReplyMapper, CourseMessageReply> implements CourseMessageReplyService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(CourseMessageReplyAddRequest params) {
        CourseMessageReply courseMessageReply = new CourseMessageReply();
        BeanUtils.copyProperties(params, courseMessageReply);
        return this.save(courseMessageReply);
    }

    @Override
    public IPage<CourseMessageReplyPageResponse> page(CourseMessageReplyPageRequest params) {
        return this.baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseMessageReplyDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseMessageReplyUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseMessageReply courseMessageReply = new CourseMessageReply();
        BeanUtils.copyProperties(params, courseMessageReply);
        return this.updateById(courseMessageReply);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

