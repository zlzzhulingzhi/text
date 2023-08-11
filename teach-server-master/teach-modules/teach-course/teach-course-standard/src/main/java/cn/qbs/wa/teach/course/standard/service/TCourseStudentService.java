package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.standard.entity.TCourseStudent;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.CourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseStudentExcelDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.TCourseStudentExcelDTO;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentAck;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentUpdateRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 课程-预报名学员(TCourseStudent)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-25 10:34:24
 */
public interface TCourseStudentService extends IService<TCourseStudent> {

    IPage<TCourseStudentPageResponse> pageCourseStudent(TCourseStudentPageRequest params);

    boolean update(TCourseStudentUpdateRequest params);

    List<TCourseStudentAck> ack(IdRequest request);

    List<TCourseStudentExcelDTO> generateExcelData(TCourseStudentPageRequest request);
    
}

