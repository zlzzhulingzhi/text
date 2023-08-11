package cn.qbs.wa.teach.organization.service.inner;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.organization.entity.Student;
import cn.qbs.wa.teach.organization.pojo.student.*;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 学员(Student)表服务接口
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
public interface StudentInnerService extends IService<Student> {

    StudentDetailResponse getStudentName(Student request);

    StudentInfoResponse getLoginInfoFromSocials(SocialLoginInfoRequest request);

    Student add(TCourseStudentAddRequest params);

}

