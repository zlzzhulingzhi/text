package cn.qbs.wa.teach.organization.service.impl.inner;

import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.organization.entity.*;
import cn.qbs.wa.teach.organization.mapper.*;
import cn.qbs.wa.teach.organization.pojo.student.*;
import cn.qbs.wa.teach.organization.service.inner.StudentInnerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 学员(Student)表服务实现类
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Slf4j
@Service("studentInnerService")
public class StudentInnerServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentInnerService {

    @Override
    public StudentDetailResponse getStudentName(Student request) {
        return baseMapper.getStudentName(request);
    }

    @Override
    public StudentInfoResponse getLoginInfoFromSocials(SocialLoginInfoRequest request) {
        return baseMapper.selectStu(request);
    }

    @Override
    public Student add(TCourseStudentAddRequest params) {
        SocialLoginInfoRequest request=new SocialLoginInfoRequest();
        request.setOrgId(params.getOrgId());
        request.setUnionId(params.getUserId().toString());
        StudentInfoResponse studentInfoResponse=baseMapper.selectStu(request);
        Student student=new Student();
        student.setPhone(AesUtil.encode(params.getPhone()));
        student.setAccount(AesUtil.encode(params.getPhone()));
        student.setUserId(params.getUserId());
        student.setRealName(params.getRealName());
        student.setOrgId(params.getOrgId());
        if (studentInfoResponse==null){
            save(student);
        }else {
            student.setId(studentInfoResponse.getId());
            updateById(student);
        }
        return student;
    }

}

