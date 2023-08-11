package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.standard.pojo.dto.orgdesk.OrgDeskTaskCourseInfoDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 【课程】(Course)表机构前台h5数据库访问层
 *
 * @author WX
 * @version 1.0
 * @date 2022-03-16 14:50:36
 */
public interface OrgDeskTaskCourseMapper extends BaseMapper<Course> {

    /**
    * 获取机构前台任务中包含的课程信息
    *
    * @param params List<IdListRequest> 课程id列表
    * @return
    */
    List<OrgDeskTaskCourseInfoDTO> getCourseList(@Param("params") IdListAndUserIdRequest params);

}

