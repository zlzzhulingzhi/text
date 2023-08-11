package cn.qbs.wa.teach.course.standard.pojo.coursemessage;

import cn.qbs.wa.teach.course.common.entity.CourseMessage;
import cn.qbs.wa.teach.course.standard.pojo.coursemessagereply.CourseMessageReplyPageResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【课程留言】(CourseMessage)分页查询【课程留言】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseMessagePageResponse extends CourseMessage {

    private IPage<CourseMessageReplyPageResponse> replyPage;
}

