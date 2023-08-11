package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.course.common.entity.CourseMessage;
import cn.qbs.wa.teach.course.standard.mapper.CourseMessageMapper;
import cn.qbs.wa.teach.course.standard.pojo.coursemessage.*;
import cn.qbs.wa.teach.course.standard.pojo.coursemessagereply.CourseMessageReplyPageRequest;
import cn.qbs.wa.teach.course.standard.service.CourseMessageReplyService;
import cn.qbs.wa.teach.course.standard.service.CourseMessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

import static cn.qbs.wa.teach.course.standard.constants.CourseConstants.*;

/**
 * 【课程留言】(CourseMessage)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Slf4j
@Service("courseMessageService")
public class CourseMessageServiceImpl extends ServiceImpl<CourseMessageMapper, CourseMessage> implements CourseMessageService {

    @Resource
    private CourseMessageReplyService courseMessageReplyService;


    @Override
    public boolean add(CourseMessageAddRequest params) {
        CourseMessage courseMessage = new CourseMessage();
        BeanUtils.copyProperties(params, courseMessage);
        // 初始状态置为未回复
        courseMessage.setReplyStatus(REPLY_STATUS_NOT_REPLY);
        return this.save(courseMessage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IPage<CourseMessagePageResponse> page(CourseMessagePageRequest params) {
        IPage<CourseMessagePageResponse> messagePage = this.baseMapper.page(params.createMpPage(), params);

        for (CourseMessagePageResponse record : messagePage.getRecords()) {
            // 查询回复记录
            CourseMessageReplyPageRequest replyPageRequest = new CourseMessageReplyPageRequest();
            replyPageRequest.setMessageId(record.getId());
            record.setReplyPage(this.courseMessageReplyService.page(replyPageRequest));
        }

        return messagePage;
    }

    @Override
    public CourseMessageDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseMessageUpdateRequest params) {
        CourseMessage courseMessage = new CourseMessage();
        BeanUtils.copyProperties(params, courseMessage);
        return this.updateById(courseMessage);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

