package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.standard.entity.TCourseStudent;
import cn.qbs.wa.teach.course.standard.mapper.TCourseStudentMapper;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.CourseStudentPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseStudentExcelDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.TCourseStudentExcelDTO;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentAck;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageResponse;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentUpdateRequest;
import cn.qbs.wa.teach.course.standard.service.CourseService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
import cn.qbs.wa.teach.course.standard.service.TCourseStudentService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.TCourseStudentAddRequestDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberTCourseStudentRequestDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 课程-预报名学员(TCourseStudent)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-25 10:34:24
 */
@Slf4j
@Service("tCourseStudentService")
public class TCourseStudentServiceImpl extends ServiceImpl<TCourseStudentMapper, TCourseStudent> implements TCourseStudentService {

    @Resource
    RemoteUnionMemberService remoteUnionMemberService;

    @Resource
    CourseService courseService;

    @Resource
    CourseStudentService courseStudentService;

    @Resource
    RemoteStudentService remoteStudentService;

    @Override
    public boolean update(TCourseStudentUpdateRequest params) {
        TCourseStudent tCourseStudent=new TCourseStudent();
        BeanUtils.copyProperties(params, tCourseStudent);
        return this.updateById(tCourseStudent);
    }

    @Transactional( rollbackFor = Exception.class)
    @Override
    public List<TCourseStudentAck> ack(IdRequest request) {
        List<TCourseStudentAck>  tCourseStudentAckList=new ArrayList<>();
        TCourseStudentAck tCourseStudentAck=new TCourseStudentAck();
        TCourseStudent tCourseStudent=new TCourseStudent();
        tCourseStudent.setStatus(Constants.DB_TRUE);
        tCourseStudent.setId(request.getId());
        //修改报名状态
        updateById(tCourseStudent);
        tCourseStudent=baseMapper.selectById(request.getId());
        tCourseStudentAck.setTCourseStudent(tCourseStudent);
        IdRequest idRequest=new IdRequest();
        idRequest.setId(tCourseStudent.getMemberId());
        UniMemberDTO uniMemberDTO=remoteUnionMemberService.info(idRequest).getRemoteData();
        TCourseStudentAddRequestDTO tCourseStudentAddRequestDTO=new TCourseStudentAddRequestDTO();
        tCourseStudentAddRequestDTO.setUserId(uniMemberDTO.getId());
        tCourseStudentAddRequestDTO.setPhone(uniMemberDTO.getPhone());
        tCourseStudentAddRequestDTO.setRealName(uniMemberDTO.getRealName());
        tCourseStudentAddRequestDTO.setOrgId(tCourseStudent.getOrgId());
        //往student表添加或者修改数据
        StudentDTO studentDTO=remoteStudentService.add(tCourseStudentAddRequestDTO).getRemoteData();
        tCourseStudentAck.setStudentDTO(studentDTO);
        CourseStudent courseStudent=new CourseStudent();
        courseStudent.setCourseId(tCourseStudent.getCourseId());
        courseStudent.setStudentId(studentDTO.getId());
        courseStudent.setUserId(tCourseStudent.getMemberId());
        courseStudent.setOrgId(tCourseStudent.getOrgId());
        List<CourseStudent> courseStudentList=courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId,tCourseStudent.getCourseId()).
                eq(CourseStudent::getUserId,tCourseStudent.getMemberId()).eq(CourseStudent::getOrgId,tCourseStudent.getOrgId()).list();
        //往course_student表添加或者修改数据
        if (courseStudentList==null || courseStudentList.isEmpty()){
            courseStudentService.save(courseStudent);
        }else {
            Long id=courseStudentList.get(Constants.DB_FAIL).getId();
            courseStudent.setId(id);
            courseStudentService.updateById(courseStudent);
        }
        tCourseStudentAck.setCourseStudent(courseStudent);
        tCourseStudentAckList.add(tCourseStudentAck);
        return tCourseStudentAckList;
    }

    @Override
    public IPage<TCourseStudentPageResponse> pageCourseStudent(TCourseStudentPageRequest params) {
        List<Long> memberIdList=new ArrayList<>();
        UniMemberTCourseStudentRequestDTO uniMemberTCourseStudentRequestDTO=new UniMemberTCourseStudentRequestDTO();
        if (StringUtils.isNotBlank(params.getRealName())){
            uniMemberTCourseStudentRequestDTO.setRealName(params.getRealName());
        }
        if (StringUtils.isNotBlank(params.getPhone())){
            uniMemberTCourseStudentRequestDTO.setPhone(params.getPhone());
        }
        //根据手机号或姓名查询memberId
        List<UniMemberDTO> uniMemberDTOList=remoteUnionMemberService.getByNameOrPhone(uniMemberTCourseStudentRequestDTO).getRemoteData();
        for (UniMemberDTO uniMemberDTO:uniMemberDTOList) {
            memberIdList.add(uniMemberDTO.getId());
        }
        params.setMemberIdList(memberIdList);
        if (StringUtils.isNotBlank(params.getRealName())||StringUtils.isNotBlank(params.getPhone())){
            if(memberIdList==null || memberIdList.isEmpty() ){
                TCourseStudentPageRequest tCourseStudentPageRequest=new TCourseStudentPageRequest();
                tCourseStudentPageRequest.setOrgId(Constants.DB_FAIL.longValue());
                IPage<TCourseStudentPageResponse> tCourseStudentPageResponseIPage=baseMapper.pageCourseStudent(tCourseStudentPageRequest.createMpPage(), tCourseStudentPageRequest);
                return tCourseStudentPageResponseIPage;
            }
        }
        IPage<TCourseStudentPageResponse> tCourseStudentPageResponseIPage=baseMapper.pageCourseStudent(params.createMpPage(), params);
        IdRequest idRequest=new IdRequest();
        for (TCourseStudentPageResponse tCourseStudentPageResponse:tCourseStudentPageResponseIPage.getRecords()) {
            idRequest.setId(tCourseStudentPageResponse.getMemberId());
            //查询预报名学员信息
            UniMemberDTO uniMemberDTO=remoteUnionMemberService.info(idRequest).getRemoteData();
            tCourseStudentPageResponse.setRealName(uniMemberDTO.getRealName());
            tCourseStudentPageResponse.setPhone(uniMemberDTO.getPhone());
            //查询课程信息
            CourseDetailResponse courseDetailResponse=courseService.getCourseName(tCourseStudentPageResponse.getCourseId());
            if(courseDetailResponse!=null){
                tCourseStudentPageResponse.setCourseName(courseDetailResponse.getCourseName());
            }
            //查询course_student
            CourseStudent courseStudent=courseStudentService.lambdaQuery().eq(CourseStudent::getOrgId,tCourseStudentPageResponse.getOrgId()).
                    eq(CourseStudent::getCourseId,tCourseStudentPageResponse.getCourseId()).
                    eq(CourseStudent::getUserId,tCourseStudentPageResponse.getMemberId()).one();
            if(courseStudent!=null){
                tCourseStudentPageResponse.setSignUpTime(courseStudent.getCreateTime());
            }
        }
        return tCourseStudentPageResponseIPage;
    }

    @Override
    public List<TCourseStudentExcelDTO> generateExcelData(TCourseStudentPageRequest request) {
        List<TCourseStudentPageResponse> tCourseStudentPageResponseList=pageCourseStudent(request).getRecords();
        return TCourseStudentServiceImpl.convertTCourseStudentPageResponseToCourseStudentExcelDTO(tCourseStudentPageResponseList);
    }

    private static List<TCourseStudentExcelDTO> convertTCourseStudentPageResponseToCourseStudentExcelDTO(List<TCourseStudentPageResponse> sourceData) {
        if (sourceData.isEmpty()) {
            return Collections.emptyList();
        }

        ArrayList<TCourseStudentExcelDTO> targetData = new ArrayList<>(sourceData.size());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        for (TCourseStudentPageResponse response : sourceData) {
            TCourseStudentExcelDTO tCourseStudentExcelDTO = new TCourseStudentExcelDTO();
            tCourseStudentExcelDTO.setRealName(response.getRealName());
            tCourseStudentExcelDTO.setPhone(StringUtils.trimToEmpty(response.getPhone()));
            if(Constants.DB_FAIL.equals(response.getStatus())){
                tCourseStudentExcelDTO.setStatus("否");
            }else{
                tCourseStudentExcelDTO.setStatus("是");
            }
            tCourseStudentExcelDTO.setCourseName(response.getCourseName());
            if (response.getSignUpTime() != null) {
                tCourseStudentExcelDTO.setSignUpTime(response.getSignUpTime().format(formatter));
            }
            targetData.add(tCourseStudentExcelDTO);
        }

        return targetData;
    }
}

