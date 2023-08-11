package cn.qbs.wa.train.logistics.service.impl.manage;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.course.api.RemoteCourseService;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CourseDetailResponseDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.lecturer.CourseLecturerDTO;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDetailResponseDTO;
import cn.qbs.wa.train.logistics.entity.*;
import cn.qbs.wa.train.logistics.mapper.*;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentPageRequest;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentPageResponse;
import cn.qbs.wa.train.logistics.pojo.organization.OrganizationDetailResponse;
import cn.qbs.wa.train.logistics.service.manage.ClazzService;
import cn.qbs.wa.train.logistics.pojo.clazz.*;
import cn.qbs.wa.train.logistics.service.manage.ClazzStudentService;
import cn.qbs.wa.train.logistics.service.manage.OrganizationManageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 班级表(Clazz)表服务实现类
 *
 * @author makejava
 * @since 2022-10-08 16:41:48
 */
@Slf4j
@Service("clazzService")
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {

    @Autowired
    OrganizationManageService organizationManageService;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    RemoteCourseService remoteCourseService;

    @Resource
    ClazzStudentMapper clazzStudentMapper;

    @Resource
    ClassroomMapper classroomMapper;

    @Autowired
    RemoteStudentService remoteStudentService;

    @Autowired
    ClazzLessonArrangeMapper clazzLessonArrangeMapper;

    @Autowired
    ClazzLessonMapper clazzLessonMapper;

    @Override
    public Clazz add(ClazzAddRequest params) {
        params.setState(Constants.DB_TRUE);
        Clazz clazz = new Clazz();
        BeanUtils.copyProperties(params, clazz);
        this.save(clazz);
        return clazz;
    }

    @Override
    public IPage<ClazzPageResponse> page(ClazzPageRequest params) {
        IPage<ClazzPageResponse> clazzPageResponseIPage=baseMapper.page(params.createMpPage(), params);
        List<ClazzPageResponse> clazzPageResponses=clazzPageResponseIPage.getRecords();
        if (!Constants.DB_FAIL.equals(clazzPageResponses.size())){
            for (ClazzPageResponse clazzPageResponse:clazzPageResponses) {
                if(clazzPageResponse.getClassroomId()!=null){
                    Classroom classroom=classroomMapper.selectById(clazzPageResponse.getClassroomId());
                    if(classroom!=null){
                        clazzPageResponse.setRoomNo(classroom.getRoomNo());
                        clazzPageResponse.setRoomType(classroom.getRoomType());
                        clazzPageResponse.setBuilding(classroom.getBuilding());
                    }
                }
                //获取机构名称
                OrganizationDetailResponse organizationDetailResponse=organizationManageService.detail(clazzPageResponse.getOrgId());
                clazzPageResponse.setOrgName(organizationDetailResponse.getName());
                IdRequest idRequest=new IdRequest();
                /*//获取员工名称
                String name=employeeMapper.getNameById(clazzPageResponse.getEmployeeId());
                clazzPageResponse.setEmployeeName(name);*/
                idRequest.setId(clazzPageResponse.getCourseId());
                //获取课程名称
                CourseDetailResponseDTO courseDetailResponseDTO=remoteCourseService.getCourse(idRequest).getRemoteData();
                if(courseDetailResponseDTO!=null){
                    clazzPageResponse.setCourseName(courseDetailResponseDTO.getCourseName());
                }
                //获取学生数
                Long count = clazzStudentMapper.selectCount(Wrappers.<ClazzStudent>lambdaQuery().eq(ClazzStudent::getOrgId, clazzPageResponse.getOrgId()).eq(ClazzStudent::getClazzId, clazzPageResponse.getId()));
                clazzPageResponse.setStudentCount(count.intValue());
            }
        }
        return clazzPageResponseIPage;
    }

    @Override
    public ClazzDetailResponse detail(ClazzDetailRequest request) {
        ClazzDetailResponse clazzDetailResponse=baseMapper.selectDetailById(request.getId());
        //获取教室类型及编号
        if(clazzDetailResponse.getClassroomId()!=null){
            Classroom classroom=classroomMapper.selectById(clazzDetailResponse.getClassroomId());
            if(classroom!=null){
                clazzDetailResponse.setRoomNo(classroom.getRoomNo());
                clazzDetailResponse.setRoomType(classroom.getRoomType());
                clazzDetailResponse.setBuilding(classroom.getBuilding());
            }
        }
        //获取机构名称
        OrganizationDetailResponse organizationDetailResponse=organizationManageService.detail(clazzDetailResponse.getOrgId());
        clazzDetailResponse.setOrgName(organizationDetailResponse.getName());
        IdRequest idRequest=new IdRequest();
        //获取员工名称
        String name=employeeMapper.getNameById(clazzDetailResponse.getEmployeeId());
        clazzDetailResponse.setEmployeeName(name);
        idRequest.setId(clazzDetailResponse.getCourseId());
        //获取课程名称
        CourseDetailResponseDTO courseDetailResponseDTO=remoteCourseService.getCourse(idRequest).getRemoteData();
        if(courseDetailResponseDTO!=null){
            clazzDetailResponse.setCourseName(courseDetailResponseDTO.getCourseName());
        }
        //获取学生名称电话号
        ClazzStudentPageRequest clazzStudentPageRequest=new ClazzStudentPageRequest();
        clazzStudentPageRequest.setClazzId(request.getId());
        clazzStudentPageRequest.setCurrent(request.getCurrent());
        clazzStudentPageRequest.setSize(request.getSize());
        //获取学生名称电话号
        IPage<ClazzStudentPageResponse> page=clazzStudentPageRequest.createMpPage();
        List<ClazzStudent> clazzStudentList=clazzStudentMapper.selectList(new LambdaQueryWrapper<ClazzStudent>().eq(ClazzStudent::getClazzId, request.getId()));
        List<ClazzStudentPageResponse> clazzStudentPageResponseList1=new ArrayList<>();
        IPage<ClazzStudentPageResponse> clazzStudentPageResponseList=clazzStudentMapper.page(clazzStudentPageRequest.createMpPage(),clazzStudentPageRequest);
        if (clazzStudentPageResponseList.getRecords()!=null && !clazzStudentPageResponseList.getRecords().isEmpty()){
            for (ClazzStudentPageResponse clazzStudentPageResponse:clazzStudentPageResponseList.getRecords()) {
                StudentDTO studentDTO=new StudentDTO();
                studentDTO.setId(clazzStudentPageResponse.getStudentId());
                if(StringUtils.isNotBlank(request.getPhone())){
                    studentDTO.setPhone(AesUtil.encode(request.getPhone()));
                }
                if(StringUtils.isNotBlank(request.getRealName())){
                    studentDTO.setRealName(request.getRealName());
                }
                //获取学员名字
                StudentDetailResponseDTO studentDetailResponseDTO=remoteStudentService.getStudentName(studentDTO).getRemoteData();
                if(studentDetailResponseDTO!=null){
                    clazzStudentPageResponse.setStudentName(studentDetailResponseDTO.getRealName());
                    clazzStudentPageResponse.setPhone(studentDetailResponseDTO.getPhone());
                    clazzStudentPageResponseList1.add(clazzStudentPageResponse);
                }
            }
        }
        page.setRecords(clazzStudentPageResponseList1);
        page.setTotal(clazzStudentList.size());
        clazzDetailResponse.setClazzStudentPageResponseList(page);
        return clazzDetailResponse;

    }

    @Override
    public boolean update(ClazzUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        if(params.getState()==null){
            if(params.getLessonNum()==null || params.getStudentNum()==null){
                this.baseMapper.updateLessonNumOrStudentNum(params);
            }
        }
        Clazz clazz = new Clazz();
        BeanUtils.copyProperties(params, clazz);
        return this.updateById(clazz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> idList) {
        for (Long id:idList) {
            ClazzStudentPageRequest clazzStudentPageRequest=new ClazzStudentPageRequest();
            clazzStudentPageRequest.setClazzId(id);
            //查询班级学员
            Long count = clazzStudentMapper.selectCount(Wrappers.<ClazzStudent>lambdaQuery().eq(ClazzStudent::getClazzId, id));
            if (count>Constants.DB_FAIL){
                throw new ServiceException("班级有学员无法删除班级");
            }
        }
        //删除班级课程及课程安排
        List<ClazzLessonArrange> clazzLessonArrangeList=clazzLessonArrangeMapper.selectList(new LambdaQueryWrapper<ClazzLessonArrange>().
                in(ClazzLessonArrange::getClazzId,idList));
        if(CollectionUtils.isNotEmpty( clazzLessonArrangeList)){
            clazzLessonArrangeMapper.deleteBatchIds(clazzLessonArrangeList);
        }
        List<ClazzLesson> clazzLessonList=clazzLessonMapper.selectList(new LambdaQueryWrapper<ClazzLesson>().
                in(ClazzLesson::getClazzId,idList));
        if(CollectionUtils.isNotEmpty(clazzLessonList)){
            clazzLessonMapper.deleteBatchIds(clazzLessonList);
        }
        return this.removeByIds(idList);
    }

    @Override
    public List<ClazzListResponse> list(ClazzListRequest params) {
        return this.baseMapper.list(params);
    }

    @Override
    public ClazzInfoResponse info(Long clazzId) {
        Long orgId = SecurityContextHolder.getOrgId();
        ClazzInfoResponse info = this.baseMapper.getInfoById(clazzId, orgId);
        // 统计班级下学员数量
        Long count = clazzStudentMapper.selectCount(Wrappers.<ClazzStudent>lambdaQuery().eq(ClazzStudent::getOrgId, orgId).eq(ClazzStudent::getClazzId, clazzId));
        info.setStudentCount(Math.toIntExact(count));
        // 远程查询课程信息
        R<CourseDetailResponseDTO> r = remoteCourseService.getCourse(new IdRequest(info.getCourseId()));
        if (r.isOk() && r.getData() != null) {
            CourseDetailResponseDTO course = r.getData();
            info.setCourseName(course.getCourseName());
            if (CollUtil.isNotEmpty(course.getLecturers())) {
                info.setLecturers(course.getLecturers().stream().map(CourseLecturerDTO::getLecturerName).collect(Collectors.toList()));
            }
        } else {
            log.error("课程服务：{}", r.isOk() ? "课程不存在" : r.getMsg());
        }
        return info;
    }

}

