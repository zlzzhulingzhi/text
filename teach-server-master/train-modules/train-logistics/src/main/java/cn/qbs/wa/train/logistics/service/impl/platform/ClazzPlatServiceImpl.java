package cn.qbs.wa.train.logistics.service.impl.platform;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.course.api.RemoteCourseService;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CourseDetailResponseDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.course.CoursePageSearchDTO;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDetailResponseDTO;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.Clazz;
import cn.qbs.wa.train.logistics.entity.ClazzLessonArrange;
import cn.qbs.wa.train.logistics.entity.ClazzStudent;
import cn.qbs.wa.train.logistics.mapper.*;
import cn.qbs.wa.train.logistics.pojo.clazz.*;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentPageRequest;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentPageResponse;
import cn.qbs.wa.train.logistics.pojo.organization.OrganizationDetailResponse;
import cn.qbs.wa.train.logistics.service.manage.OrganizationManageService;
import cn.qbs.wa.train.logistics.service.platform.ClazzPlatService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 班级表(Clazz)表服务实现类
 *
 * @author makejava
 * @since 2022-10-08 16:41:48
 */
@Slf4j
@Service("clazzPlatService")
public class ClazzPlatServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzPlatService {

    @Autowired
    OrganizationManageService organizationManageService;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    RemoteCourseService remoteCourseService;

    @Resource
    private ClazzStudentMapper clazzStudentMapper;

    @Autowired
    RemoteStudentService remoteStudentService;

    @Resource
    ClassroomMapper classroomMapper;

    @Resource
    ClazzLessonArrangeMapper clazzLessonArrangeMapper;

    @Override
    public IPage<ClazzPageResponse> page(ClazzPageRequest params) {
        IPage<ClazzPageResponse> clazzPageResponseIPage=baseMapper.pageV2(params.createMpPage(), params);
        List<ClazzPageResponse> clazzPageResponses=clazzPageResponseIPage.getRecords();
        if (CollectionUtils.isNotEmpty(clazzPageResponses)){
            for (ClazzPageResponse clazzPageResponse:clazzPageResponses) {
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
    public IPage<ClazzPageResponse> pageV2(ClazzPageRequest params) {
        if(StringUtils.isNotEmpty(params.getLecturerName())){
            List<ClazzLessonArrange> clazzLessonArrangeList=clazzLessonArrangeMapper.selectList(new LambdaQueryWrapper<ClazzLessonArrange>().
                    like(ClazzLessonArrange::getLecturerName,params.getLecturerName()));
            List<Long> idList = clazzLessonArrangeList.stream().map(ClazzLessonArrange::getClazzId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(idList)){
                return params.createMpPage();
            }
            params.setIdList(idList);
        }
        if(StringUtils.isNotEmpty(params.getCourseName())){
            CoursePageSearchDTO coursePageSearchDTO=new CoursePageSearchDTO();
            coursePageSearchDTO.setCourseName(params.getCourseName());
            coursePageSearchDTO.setOrgId(params.getOrgId());
            List<Long> courseIdList=remoteCourseService.getIdList(coursePageSearchDTO).getRemoteData();
            params.setCourseIdList(courseIdList);
        }
        IPage<ClazzPageResponse> clazzPageResponseIPage=baseMapper.pageV2(params.createMpPage(), params);
        List<ClazzPageResponse> clazzPageResponses=clazzPageResponseIPage.getRecords();
        if (CollectionUtils.isNotEmpty(clazzPageResponses)){
            for (ClazzPageResponse clazzPageResponse:clazzPageResponses) {
                //获取教室信息
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
                clazzPageResponse.setPhone(AesUtil.decode(clazzPageResponse.getPhone()));
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
    public IPage<ClazzSummaryResponse> getClazzSummary(ClazzSummaryRequest params) {
        IPage<ClazzSummaryResponse> clazzSummaryResponseIPage=baseMapper.getClazzSummary(params.createMpPage(), params);
        if(CollectionUtils.isEmpty(clazzSummaryResponseIPage.getRecords())){
            return params.createMpPage();
        }
        List<ClazzSummaryResponse> clazzSummaryResponseList=baseMapper.getClazzSummaryV2(params);
        if(CollectionUtils.isNotEmpty(clazzSummaryResponseList)){
            Map<Long, Long> map=clazzSummaryResponseList.stream().collect(Collectors.toMap(ClazzSummaryResponse::getOrgId, ClazzSummaryResponse::getStudentCount));
            for (ClazzSummaryResponse clazzSummaryResponse:clazzSummaryResponseIPage.getRecords()) {
                if(clazzSummaryResponse.getLessonNumCount()==null){
                    clazzSummaryResponse.setLessonNumCount(0L);
                }else {
                    clazzSummaryResponse.setLessonNumCount(clazzSummaryResponse.getLessonNumCount());
                }
                if(map.get(clazzSummaryResponse.getOrgId())==null){
                    clazzSummaryResponse.setStudentCount(0L);
                }else {
                    clazzSummaryResponse.setStudentCount(map.get(clazzSummaryResponse.getOrgId()));
                }
            }
        }else {
            for (ClazzSummaryResponse clazzSummaryResponse:clazzSummaryResponseIPage.getRecords()) {
                clazzSummaryResponse.setStudentCount(0L);
                clazzSummaryResponse.setLessonNumCount(0L);
            }
        }
        return clazzSummaryResponseIPage;
    }

    @Override
    public ClazzSummaryResponse getClazzTotalSummary(ClazzSummaryRequest params) {
        ClazzSummaryResponse clazzSummaryResponse=new ClazzSummaryResponse();
        List<Clazz> clazzList=this.lambdaQuery().eq(params.getOrgId()!=null,Clazz::getOrgId,params.getOrgId()).
                ge(params.getStartDate()!=null,Clazz::getStartDate,params.getStartDate()).
                le(params.getEndDate()!=null,Clazz::getEndDate,params.getEndDate()).list();
        if(CollectionUtils.isNotEmpty(clazzList)){
            clazzSummaryResponse.setClazzCount(new Long((long)clazzList.size()));
            List<Long> clazzIds=clazzList.stream().map(Clazz::getId).collect(Collectors.toList());
            Long count=clazzStudentMapper.selectCount(Wrappers.<ClazzStudent>lambdaQuery().in(ClazzStudent::getClazzId,clazzIds));
            clazzSummaryResponse.setStudentCount(count);
        }else {
            clazzSummaryResponse.setClazzCount(0L);
            clazzSummaryResponse.setStudentCount(0L);
        }
        return clazzSummaryResponse;
    }

    @Override
    public List<IntegrateClazzResponse> listClazzByMemberId(Long memberId) {
        return baseMapper.listClazzByMemberId(memberId);
    }
}

