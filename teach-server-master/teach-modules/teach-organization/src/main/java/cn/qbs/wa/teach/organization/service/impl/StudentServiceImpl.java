package cn.qbs.wa.teach.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.qbs.wa.teach.basic.api.RemoteDictService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.dict.DictPageRequestDTO;
import cn.qbs.wa.teach.common.core.config.RSAConfig;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.course.api.RemoteCourseStudentService;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursestudent.CourseStudentDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursestudent.CourseStudentQueryDTO;
import cn.qbs.wa.teach.organization.entity.*;
import cn.qbs.wa.teach.organization.enums.AccountTypeEnum;
import cn.qbs.wa.teach.organization.enums.BusinessTypeEnum;
import cn.qbs.wa.teach.organization.enums.SexEnum;
import cn.qbs.wa.teach.organization.enums.SocialPlatformEnum;
import cn.qbs.wa.teach.organization.excel.*;
import cn.qbs.wa.teach.organization.mapper.*;
import cn.qbs.wa.teach.organization.pojo.appoauth.AppOAuthUserRequest;
import cn.qbs.wa.teach.organization.pojo.employee.PersonalPasswordUpdateRequest;
import cn.qbs.wa.teach.organization.pojo.employee.PersonalPhoneUpdateRequest;
import cn.qbs.wa.teach.organization.pojo.groups.GroupsDetailResponse;
import cn.qbs.wa.teach.organization.pojo.importrecord.ImportCountResponse;
import cn.qbs.wa.teach.organization.pojo.student.*;
import cn.qbs.wa.teach.organization.pojo.studentdept.StudentDeptSingleAddRequest;
import cn.qbs.wa.teach.organization.pojo.studentgroup.StudentGroupAddRequest;
import cn.qbs.wa.teach.organization.service.*;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberAddDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberQueryDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import cn.qbs.wa.train.logistics.api.RemoteTrainClazzService;
import cn.qbs.wa.train.logistics.api.RemoteTrainClazzStudentService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.*;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学员(Student)表服务实现类
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
@Slf4j
@Service("studentService")
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private RemoteDictService remoteDictService;

    @Resource
    private AppOauthService appOauthService;

    @Resource
    private StudentDeptService studentDeptService;

    @Resource
    private StudentDataValidateFactory studentDataValidateFactory;

    @Resource
    private GroupsMapper groupsMapper;

    @Resource
    private StudentGroupService studentGroupService;

    @Resource
    private DeptMapper deptMapper;

    @Resource
    private StudentDeptMapper studentDeptMapper;

    @Resource
    private ImportRecordService importRecordService;

    @Resource
    private ImportRecordDetailService importRecordDetailService;

    @Resource
    private StudentGroupMapper studentGroupMapper;

    @Resource
    private RemoteUnionMemberService remoteUnionMemberService;

    @Resource
    private RemoteTrainClazzStudentService remoteTrainClazzStudentService;

    @Resource
    RemoteTrainClazzService remoteTrainClazzService;

    @Resource
    RemoteCourseStudentService remoteCourseStudentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginUser add(StudentAddRequest params) {
        // 先创建一个User
        Student student = new Student();
        // 注册创建学员账号
        String phone = params.getAccount();
        // 远程注册统一会员账号信息
        UniMemberAddDTO uniMemberAddDTO = new UniMemberAddDTO(phone);
        uniMemberAddDTO.setRealName(params.getRealName());
        uniMemberAddDTO.setSex(params.getSex());
        uniMemberAddDTO.setEmail(params.getEmail());
        uniMemberAddDTO.setIdNumber(params.getIdNumber());
        R<UniMemberDTO> r = remoteUnionMemberService.register(uniMemberAddDTO);
        if (!r.isOk()) {
            throw new ServiceException(r.getMsg());
        }
        Long userId = r.getData().getId();
        Long count = this.lambdaQuery().eq(Student::getUserId, userId).count();
        if (count != null && count > 0) {
            throw new ServiceException(String.format("手机号：%s 存在注册记录，请联系系统管理员", phone));
        }
        encodeInfo(params);
        BeanUtil.copyProperties(params, student);
        student.setUserId(userId);

        student.setIdentity(1);
        if (params.getDeptId() != null) {
            student.setIdentity(2);
            this.save(student);
            StudentDeptSingleAddRequest studentDeptSingleAddRequest = new StudentDeptSingleAddRequest();
            studentDeptSingleAddRequest.setStudentId(student.getId());
            studentDeptSingleAddRequest.setDeptId(params.getDeptId());
            studentDeptService.add(studentDeptSingleAddRequest);
        } else {
            this.save(student);
        }
        //添加学员标签
        if (CollectionUtils.isNotEmpty(params.getGroupIdList())) {
            StudentGroupAddRequest studentGroupAddRequest = new StudentGroupAddRequest();
            studentGroupAddRequest.setStudentId(student.getId());
            studentGroupAddRequest.setOrgId(params.getOrgId());
            studentGroupAddRequest.setGroupIds(params.getGroupIdList());
            studentGroupService.add(studentGroupAddRequest );

        }
        return initLoginUser(BeanUtil.copyProperties(student, LoginInfoResponse.class));
    }

    @Override
    public IPage<StudentPageResponse> pageV2(StudentPageRequest params) {
        if (StringUtils.isNotBlank(params.getPhone())) {
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        if (StringUtils.isNotBlank(params.getIdNumber())) {
            params.setIdNumber(AesUtil.encode(params.getIdNumber()));
        }
        Long noDept = -1L;
        if (CollUtil.isNotEmpty(params.getDeptIdList()) && params.getDeptIdList().contains(noDept)) {
            params.setDeptId(noDept);
            params.setDeptIdList(null);
        }
        params.setOrgId(SecurityContextHolder.getOrgId());
        IPage<StudentPageResponse> page;
        if (params.getGroupId() != null) {
            page = baseMapper.pageByGroup(params.createMpPage(), params);
        } else if(CollUtil.isNotEmpty(params.getGroupIdList())){
            page = baseMapper.pageByGroupIdList(params.createMpPage(), params);
        } else{
            page = baseMapper.page(params.createMpPage(), params);
        }
        return page;
    }

    @Override
    public IPage<StudentPageResponse> page(StudentPageRequest params) {
        if (StringUtils.isNotBlank(params.getPhone())) {
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        if (StringUtils.isNotBlank(params.getIdNumber())) {
            params.setIdNumber(AesUtil.encode(params.getIdNumber()));
        }
        Long noDept = -1L;
        if (CollUtil.isNotEmpty(params.getDeptIdList()) && params.getDeptIdList().contains(noDept)) {
            params.setDeptId(noDept);
            params.setDeptIdList(null);
        }
        params.setOrgId(SecurityContextHolder.getOrgId());
        IPage<StudentPageResponse> page = null;
        if (params.getGroupId() != null) {
            page = baseMapper.pageByGroup(params.createMpPage(), params);
        } else {
            page = baseMapper.page(params.createMpPage(), params);
        }
        if(page.getRecords()!=null && !page.getRecords().isEmpty()){
            IdListRequest idListRequest=new IdListRequest();
            List<Long> idList=new ArrayList<>();
            for (StudentPageResponse record : page.getRecords()) {
                idList.add(record.getUserId());
            }
            idListRequest.setIdList(idList);
            List<UniMemberDTO> uniMemberDTOList=remoteUnionMemberService.list(idListRequest).getRemoteData();
            for (StudentPageResponse studentPageResponse:page.getRecords()) {
                if(uniMemberDTOList!=null && !uniMemberDTOList.isEmpty()){
                    for (UniMemberDTO uniMemberDTO:uniMemberDTOList) {
                        if(studentPageResponse.getUserId().equals(uniMemberDTO.getId())){
                            studentPageResponse.setEmail(uniMemberDTO.getEmail());
                            studentPageResponse.setEducation(uniMemberDTO.getEducation());
                            studentPageResponse.setNativePlace(uniMemberDTO.getNativePlace());
                            studentPageResponse.setWorkUnit(uniMemberDTO.getWorkUnit());
                        }
                    }
                }
            }
        }
        return setGroupNameList(page);
    }

    private IPage<StudentPageResponse> setGroupNameList(IPage<StudentPageResponse> page) {
        if (page == null || page.getRecords() == null) {
            return new Page<>();
        }
        for (StudentPageResponse record : page.getRecords()) {
            List<GroupsDetailResponse> groupsDetailResponses = groupsMapper.selectDetailByIds(record.getId());
            List<String> collect = groupsDetailResponses.stream().map(GroupsDetailResponse::getGroupName).collect(Collectors.toList());
            record.setGroupNameList(collect);
        }
        return page;
    }

    @Override
    public IPage<StudentPageResponse> pageNoTenant(StudentPageRequest params) {
        if (StringUtils.isNotBlank(params.getPhone())) {
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        return baseMapper.pageNoTenant(params.createMpPage(), params);
    }

    @Override
    public List<StudentListResponse> list(StudentListRequest params) {
        if (StringUtils.isNotBlank(params.getPhone())) {
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        return baseMapper.list(params);
    }

    @Override
    public StudentDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(StudentUpdateRequest params) {
        Student student = new Student();
        encodeInfo(params);
        BeanUtils.copyProperties(params, student);
        if (params.getDeptId() != null) {
            student.setIdentity(2);
            StudentDeptSingleAddRequest studentDeptSingleAddRequest = new StudentDeptSingleAddRequest();
            studentDeptSingleAddRequest.setStudentId(params.getId());
            studentDeptSingleAddRequest.setDeptId(params.getDeptId());
            studentDeptService.add(studentDeptSingleAddRequest);
            studentDeptService.courseAddStudent(params.getDeptId());
            studentDeptService.examAddStudent(params.getDeptId());
        }
        return this.updateById(student);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public LoginUser getLoginInfo(LoginInfoRequest request) {
        LoginInfoResponse student = this.baseMapper.getLoginInfo(request.getOrgId(), AesUtil.encode(request.getAccount()));
        return initLoginUser(student);
    }

    @Override
    public LoginUser getLoginInfoFromSocial(SocialLoginInfoRequest request) {
        AppOAuthUserRequest authUserRequest = new AppOAuthUserRequest();
        authUserRequest.setOrgId(request.getOrgId());
        authUserRequest.setUid(request.getUnionId());
        authUserRequest.setAccountType(AccountTypeEnum.STUDENT.getName());
        authUserRequest.setPlatform(SocialPlatformEnum.WEIXIN.getName());
        LoginInfoResponse student = appOauthService.getLoginInfo(authUserRequest);
        return initLoginUser(student);
    }

    @Override
    public LoginUser getLoginInfoByUserID(Long userId) {
        Student one = this.lambdaQuery().eq(Student::getUserId, userId).one();
        if (one == null) {
            return null;
        }
        return initLoginUser(BeanUtil.copyProperties(one, LoginInfoResponse.class));
    }

    @Override
    public IPage<StudentToClazz> getClazzOrgList(StudentPageRequest params) {
        if(StringUtils.isNotEmpty(params.getPhone())){
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        Page<StudentToClazz> page = params.createMpPage();
        ClazzStudentPageRequestDTO clazzStudentPageRequestDTO=new ClazzStudentPageRequestDTO();
        clazzStudentPageRequestDTO.setOrgId(SecurityContextHolder.getOrgId());
        clazzStudentPageRequestDTO.setClazzId(params.getClazzId());
        //获取班级学生信息
        List<ClazzStudentPageResponseDTO> clazzStudentPageResponseDTOList=remoteTrainClazzStudentService.getByOrgId(clazzStudentPageRequestDTO).getRemoteData();
        if(Constants.DB_TRUE.equals(params.getByCourseId())){
            List<StudentToClazz> studentToClazzList=new ArrayList<>();
           /* if(clazzStudentPageResponseDTOList==null ||clazzStudentPageResponseDTOList.isEmpty()){
                return studentToClazzList;
            }*/
            ClazzPageRequestDTO clazzPageRequestDTO=new ClazzPageRequestDTO();
            clazzPageRequestDTO.setOrgId(params.getOrgId());
            clazzPageRequestDTO.setClazzId(params.getClazzId());
            //获取课程id
            List<ClazzPageResponseDTO> clazzPageResponseDTOList=remoteTrainClazzService.getByOrgId(clazzPageRequestDTO).getRemoteData();
            IdRequest idRequest=new IdRequest();
            idRequest.setId(clazzPageResponseDTOList.get(Constants.DB_FAIL).getCourseId());
            //获取学生id
            List<CourseStudentDTO> courseStudentDTOList=remoteCourseStudentService.getCourseStudent(idRequest).getRemoteData();
            if(courseStudentDTOList==null || courseStudentDTOList.isEmpty()){
                return page;
            }
            List<Long> idList=new ArrayList<>();
            for (CourseStudentDTO courseStudentDTO:courseStudentDTOList) {
                idList.add(courseStudentDTO.getStudentId());
            }
            //获取学生列表
            params.setStudentIdList(idList);
            IPage<StudentPageResponse> students=baseMapper.page(params.createMpPage(),params);

            /*List<Long> idList=new ArrayList<>();
            for (ClazzStudentPageResponseDTO clazzStudentPageResponseDTO:clazzStudentPageResponseDTOList) {
                idList.add(clazzStudentPageResponseDTO.getStudentId());
            }
            //获取学生列表
            List<Student> students=baseMapper.selectBatchIds(idList);*/
            //List<Student> studentList = page(students,params.getCurrent(), params.getSize());
            if(students.getRecords()!=null && !students.getRecords().isEmpty()){
                for (StudentPageResponse student:students.getRecords()) {
                    //IdRequest idRequest=new IdRequest();
                    idRequest.setId(student.getUserId());
                    UniMemberDTO uniMemberDTO=remoteUnionMemberService.info(idRequest).getRemoteData();
                    StudentToClazz studentToClazz=new StudentToClazz();
                    studentToClazz.setUnitName(uniMemberDTO.getWorkUnit());
                    studentToClazz.setPhone(AesUtil.decode(student.getPhone()));
                    studentToClazz.setStudentId(student.getId());
                    studentToClazz.setRealName(student.getRealName());
                    studentToClazz.setUserId(student.getUserId());
                    //studentToClazz.setAdded(Constants.DB_TRUE);
                    studentToClazzList.add(studentToClazz);
                }
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(clazzStudentPageResponseDTOList)){
                    for (StudentToClazz studentToClazz:studentToClazzList) {
                        for (ClazzStudentPageResponseDTO clazzStudentPageResponseDTO:clazzStudentPageResponseDTOList) {
                            if(studentToClazz.getStudentId().equals(clazzStudentPageResponseDTO.getStudentId())){
                                studentToClazz.setAdded(Constants.DB_TRUE);
                            }
                        }
                    }
                }
                page.setTotal(students.getTotal());
            }
            page.setRecords(studentToClazzList);
            return page;
        }else {
            //获取学生列表
            params.setOrgId(SecurityContextHolder.getOrgId());
            params.setEnabled(Constants.DB_TRUE);
            IPage<StudentPageResponse> students=baseMapper.page(params.createMpPage(),params);
            List<StudentToClazz> studentToClazzList=new ArrayList<>();
            if(students.getRecords()!=null && !students.getRecords().isEmpty()){
                for (StudentPageResponse student:students.getRecords()) {
                    IdRequest idRequest=new IdRequest();
                    idRequest.setId(student.getUserId());
                    UniMemberDTO uniMemberDTO=remoteUnionMemberService.info(idRequest).getRemoteData();
                    StudentToClazz studentToClazz=new StudentToClazz();
                    studentToClazz.setUnitName(uniMemberDTO.getWorkUnit());
                    studentToClazz.setPhone(AesUtil.decode(student.getPhone()));
                    studentToClazz.setStudentId(student.getId());
                    studentToClazz.setRealName(student.getRealName());
                    studentToClazz.setUserId(student.getUserId());
                    studentToClazzList.add(studentToClazz);
                }
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(clazzStudentPageResponseDTOList)){
                    for (StudentToClazz studentToClazz:studentToClazzList) {
                        for (ClazzStudentPageResponseDTO clazzStudentPageResponseDTO:clazzStudentPageResponseDTOList) {
                            if(studentToClazz.getStudentId().equals(clazzStudentPageResponseDTO.getStudentId())){
                                studentToClazz.setAdded(Constants.DB_TRUE);
                            }
                        }
                    }
                }
                page.setTotal(students.getTotal());
            }
            page.setRecords(studentToClazzList);
            return page;
        }

    }

    @Override
    public IPage<StudentToClazz> getOrgList(StudentPageRequest params) {
        if(StringUtils.isNotEmpty(params.getPhone())){
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        Page<StudentToClazz> page = params.createMpPage();
        ClazzStudentPageRequestDTO clazzStudentPageRequestDTO=new ClazzStudentPageRequestDTO();
        clazzStudentPageRequestDTO.setOrgId(SecurityContextHolder.getOrgId());
        clazzStudentPageRequestDTO.setClazzId(params.getClazzId());
        //获取班级学生信息
        List<ClazzStudentPageResponseDTO> clazzStudentPageResponseDTOList=remoteTrainClazzStudentService.getByOrgId(clazzStudentPageRequestDTO).getRemoteData();
        if(Constants.DB_TRUE.equals(params.getByCourseId())){
            List<StudentToClazz> studentToClazzList=new ArrayList<>();
            if(clazzStudentPageResponseDTOList==null ||clazzStudentPageResponseDTOList.isEmpty()){
                return page;
            }
           /* ClazzPageRequestDTO clazzPageRequestDTO=new ClazzPageRequestDTO();
            clazzPageRequestDTO.setOrgId(params.getOrgId());
            clazzPageRequestDTO.setClazzId(params.getClazzId());
            //获取课程id
            List<ClazzPageResponseDTO> clazzPageResponseDTOList=remoteTrainClazzService.getByOrgId(clazzPageRequestDTO).getRemoteData();
            IdRequest idRequest=new IdRequest();
            idRequest.setId(clazzPageResponseDTOList.get(Constants.DB_FAIL).getCourseId());
            //获取学生id
            List<CourseStudentDTO> courseStudentDTOList=remoteCourseStudentService.getCourseStudent(idRequest).getRemoteData();
            List<Long> idList=new ArrayList<>();
            for (CourseStudentDTO courseStudentDTO:courseStudentDTOList) {
                idList.add(courseStudentDTO.getStudentId());
            }
            //获取学生列表
            List<Student> students=baseMapper.selectBatchIds(idList);*/
            List<Long> idList=new ArrayList<>();
            for (ClazzStudentPageResponseDTO clazzStudentPageResponseDTO:clazzStudentPageResponseDTOList) {
                idList.add(clazzStudentPageResponseDTO.getStudentId());
            }
            //获取学生列表
            params.setStudentIdList(idList);
            IPage<StudentPageResponse> students=baseMapper.page(params.createMpPage(),params);
            if(students.getRecords()!=null && !students.getRecords().isEmpty()){
                for (StudentPageResponse student:students.getRecords()) {
                    IdRequest idRequest=new IdRequest();
                    idRequest.setId(student.getUserId());
                    UniMemberDTO uniMemberDTO=remoteUnionMemberService.info(idRequest).getRemoteData();
                    StudentToClazz studentToClazz=new StudentToClazz();
                    studentToClazz.setUnitName(uniMemberDTO.getWorkUnit());
                    studentToClazz.setPhone(AesUtil.decode(student.getPhone()));
                    studentToClazz.setStudentId(student.getId());
                    studentToClazz.setRealName(student.getRealName());
                    studentToClazz.setUserId(student.getUserId());
                    studentToClazz.setAdded(Constants.DB_TRUE);
                    studentToClazzList.add(studentToClazz);
                }
                page.setTotal(students.getTotal());
            }
            /*if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(clazzStudentPageResponseDTOList)){
                for (StudentToClazz studentToClazz:studentToClazzList) {
                    for (ClazzStudentPageResponseDTO clazzStudentPageResponseDTO:clazzStudentPageResponseDTOList) {
                        if(studentToClazz.getStudentId().equals(clazzStudentPageResponseDTO.getStudentId())){
                            studentToClazz.setAdded(Constants.DB_TRUE);
                        }
                    }
                }
            }*/
            page.setRecords(studentToClazzList);
            return page;
        }else {
            //获取学生列表
            params.setOrgId(SecurityContextHolder.getOrgId());
            params.setEnabled(Constants.DB_TRUE);
            IPage<StudentPageResponse> students=baseMapper.page(params.createMpPage(),params);
            List<StudentToClazz> studentToClazzList=new ArrayList<>();
            if(students.getRecords()!=null && !students.getRecords().isEmpty()){
                for (StudentPageResponse student:students.getRecords()) {
                    IdRequest idRequest=new IdRequest();
                    idRequest.setId(student.getUserId());
                    UniMemberDTO uniMemberDTO=remoteUnionMemberService.info(idRequest).getRemoteData();
                    StudentToClazz studentToClazz=new StudentToClazz();
                    studentToClazz.setUnitName(uniMemberDTO.getWorkUnit());
                    studentToClazz.setPhone(AesUtil.decode(student.getPhone()));
                    studentToClazz.setStudentId(student.getId());
                    studentToClazz.setRealName(student.getRealName());
                    studentToClazz.setUserId(student.getUserId());
                    studentToClazzList.add(studentToClazz);
                }
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(clazzStudentPageResponseDTOList)){
                    for (StudentToClazz studentToClazz:studentToClazzList) {
                        for (ClazzStudentPageResponseDTO clazzStudentPageResponseDTO:clazzStudentPageResponseDTOList) {
                            if(studentToClazz.getStudentId().equals(clazzStudentPageResponseDTO.getStudentId())){
                                studentToClazz.setAdded(Constants.DB_TRUE);
                            }
                        }
                    }
                }
                page.setTotal(students.getTotal());
            }
            page.setRecords(studentToClazzList);
            return page;
        }

    }

    private LoginUser initLoginUser(LoginInfoResponse student) {
        if (student != null) {
            if (Constants.DB_FAIL.equals(student.getEnabled())) {
                throw new ServiceException("当前账号已被禁用");
            }
            LoginUser loginUser = new LoginUser();
            loginUser.setStudentId(student.getId());
            loginUser.setUserid(student.getUserId());
            loginUser.setEmployeeId(student.getEmployeeId());
            loginUser.setUsername(student.getRealName());
            loginUser.setOrgId(student.getOrgId());
            loginUser.setHeadImgUrl(student.getHeadImgUrl());
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(student, sysUser);
            sysUser.setUserName(student.getRealName());
            sysUser.setPhonenumber(AesUtil.decode(student.getAccount()));
            loginUser.setSysUser(sysUser);
            return loginUser;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginUser bindingSocial(SocialBindingRequest request) {
        String uid = request.getUnionId();
        Long count = appOauthService.lambdaQuery().eq(AppOauth::getUid, uid).count();
        if (count != null && count > 0) {
            throw new ServiceException("该微信已绑定其他学员账号");
        }
        LoginUser loginUser = null;
        Long userId = request.getUserId();
        if (userId == null) {
            if (StrUtil.isBlank(request.getAccount())) {
                throw new ServiceException("手机号不能为空");
            }
            // 先查找手机号是否存在学员,没有则创建
            loginUser = getLoginInfo(new LoginInfoRequest(request.getOrgId(), request.getAccount()));
            if (loginUser == null) {
                loginUser = add(request);
            } else {
                if (!StrUtil.isAllNotBlank(loginUser.getHeadImgUrl(), loginUser.getUsername())) {
                    Student student = new Student();
                    student.setId(loginUser.getStudentId());
                    if (StrUtil.isBlank(loginUser.getHeadImgUrl())) {
                        student.setHeadImgUrl(StrUtil.trimToNull(request.getHeadImgUrl()));
                    }
                    if (StrUtil.isBlank(loginUser.getUsername())) {
                        student.setRealName(StrUtil.trimToNull(request.getRealName()));
                    }
                    this.updateById(student);
                }
            }
            userId = loginUser.getUserid();
        }
        AppOauth appOauth = new AppOauth();
        appOauth.setOrgId(request.getOrgId());
        appOauth.setUid(request.getUnionId());
        appOauth.setUserId(userId);
        appOauth.setAccountType(AccountTypeEnum.STUDENT.getName());
        appOauth.setPlatform(SocialPlatformEnum.WEIXIN.getName());
        try {
            appOauthService.save(appOauth);
        } catch (Exception e) {
            throw new ServiceException("该手机号已绑定其他微信，请更换其他手机号进行绑定，或直接使用该手机号验证码登录");
        }
        return loginUser;
    }

    @Override
    public Boolean getPhoneBindingInfo(LoginInfoRequest request) {
        SocialLoginInfoResponse phoneBindingInfo = this.baseMapper.getPhoneBindingInfo(request.getOrgId(), AesUtil.encode(request.getAccount()));
        if (phoneBindingInfo == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean unbindSocial(Long studentId) {
        Student student = this.getById(studentId);
        if (student == null) {
            return Boolean.FALSE;
        }
        appOauthService.remove(Wrappers.<AppOauth>lambdaQuery()
                .eq(AppOauth::getOrgId, student.getOrgId())
                .eq(AppOauth::getUserId, student.getUserId())
                .eq(AppOauth::getAccountType, AccountTypeEnum.STUDENT.getName())
                .eq(AppOauth::getPlatform, SocialPlatformEnum.WEIXIN.getName())
        );
        return Boolean.TRUE;
    }

    @Override
    public boolean changePhone(PersonalPhoneUpdateRequest params) {
        Student student = this.getById(params.getId());
        if (student == null) {
            throw new ServiceException("无法找到该用户");
        }
        String encodePhone = AesUtil.encode(params.getPhone());
        if (student.getAccount().equals(encodePhone)) {
            throw new ServiceException("新旧手机号一致，无需更改");
        }
        Optional<Student> opt = this.lambdaQuery().eq(Student::getAccount, encodePhone).oneOpt();
        if (opt.isPresent()) {
            throw new ServiceException("该手机号已存在");
        }
        Student update = new Student();
        update.setId(params.getId());
        update.setAccount(encodePhone);
        update.setPhone(encodePhone);
        return this.updateById(update);
    }

    @Override
    public boolean changePassword(PersonalPasswordUpdateRequest params) {


        params.setNewPassword(decodePassWord(params.getNewPassword()));
        params.setOldPassword(decodePassWord(params.getOldPassword()));
        if (!params.getNewPassword().equals(params.getConfirmPassword())) {
            throw new ServiceException("两次输入新密码不一致");
        }

        if (params.getNewPassword().equals(params.getOldPassword())) {
            throw new ServiceException("新旧密码一致，无需更改");
        }
        Student student = this.getById(params.getId());
        if (student == null) {
            throw new ServiceException("无法找到该用户");
        }

        // 密码加密后进行对比
        if (!SecurityUtils.matchesPassword(params.getOldPassword(), student.getPassword())) {
            throw new ServiceException("旧密码不正确");
        }

        Student update = new Student();
        update.setId(student.getId());
        update.setPassword(SecurityUtils.encryptPassword(params.getNewPassword()));
        this.updateById(update);
        return true;
    }

    private String decodePassWord(String password) {
        if (StrUtil.isNotBlank(password)) {
            RSA rsa = new RSA(RSAConfig.privateKey, RSAConfig.publicKey);
            byte[] decode = Base64.getDecoder().decode(password);
            byte[] decrypt = rsa.decrypt(decode, KeyType.PrivateKey);
            return new String(decrypt);
        }
        return null;
    }

    @Override
    public boolean resetPassword(PasswordResetRequest params) {
        LoginInfoResponse loginInfo = this.baseMapper.getLoginInfo(params.getOrgId(), AesUtil.encode(params.getAccount()));
        if (loginInfo != null) {
            params.setPassword(decodePassWord(params.getPassword()));
            Student update = new Student();
            update.setId(loginInfo.getId());
            update.setPassword(SecurityUtils.encryptPassword(params.getPassword()));
            this.updateById(update);
            return true;
        } else {
            throw new ServiceException("用户不存在");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ImportCountResponse batchSave(List<StudentAddRequest> collect) {
        for (StudentAddRequest params : collect) {
            if (!params.getPassed()) {
                continue;
            }
            // 注册创建学员账号
            // 远程注册统一会员账号信息
            UniMemberAddDTO uniMemberAddDTO=new UniMemberAddDTO();
            uniMemberAddDTO.setAccount(params.getAccount());
            uniMemberAddDTO.setRealName(params.getRealName());
            uniMemberAddDTO.setIdNumber(params.getIdNumber());
            uniMemberAddDTO.setSex(params.getSex());
            R<UniMemberDTO> userResult = remoteUnionMemberService.register(uniMemberAddDTO);
            if (!userResult.isOk()) {
                throw new ServiceException(userResult.getMsg());
            }
            encodeInfo(params);
            Student student = BeanUtil.copyProperties(params, Student.class);
            Long userId = userResult.getData().getId();
            student.setUserId(userId);
            // 再创建一个Student
            try {
                this.save(student);
                if (StringUtils.isNotBlank(params.getGroupName())) {
                    Groups groups = groupsMapper.selectOne(new QueryWrapper<Groups>().eq("group_name", params.getGroupName()));
                    if (groups != null) {
                        StudentGroupAddRequest studentGroupAddRequest = new StudentGroupAddRequest();
                        studentGroupAddRequest.setStudentId(student.getId());
                        studentGroupAddRequest.setGroupIds(ListUtil.toList((groups.getId())));
                        studentGroupService.add(studentGroupAddRequest);
                    }
                }
            } catch (Exception e) {
                throw new ServiceException(AesUtil.decode(student.getPhone())+"在excel中有多个，请确保手机号不重复");
            }
            //获取子部门
            if (StringUtils.isNotBlank(params.getDeptName())) {
                String[] split = params.getDeptName().split("/");
                String deptName = split[split.length - 1];
                Dept dept = deptMapper.selectOne(new QueryWrapper<Dept>().eq("dept_name", deptName));
                if (dept != null) {
                    StudentDeptSingleAddRequest studentDeptSingleAddRequest = new StudentDeptSingleAddRequest();
                    studentDeptSingleAddRequest.setDeptId(dept.getId());
                    studentDeptSingleAddRequest.setStudentId(student.getId());
                    studentDeptService.add(studentDeptSingleAddRequest);

//                    Long count1 = studentDeptService.lambdaQuery().eq(StudentDept::getDeptId, dept.getId()).count();
//                    if (count1 != null && count1 > 0) {
//                        deptService.update(new LambdaUpdateWrapper<Dept>().set(Dept::getStudentCount, count1).eq(Dept::getId, dept.getId()));
//                    }
                }
            }
        }
        //保存导入记录并返回
        return saveImportRecord(collect);
    }

    private ImportCountResponse saveImportRecord(List<StudentAddRequest> collect) {
        //提取失败数据计算
        List<StudentAddRequest> failureList = collect.stream().filter(i -> !i.getPassed()).collect(Collectors.toList());
        //set返回值
        ImportCountResponse importCountResponse = new ImportCountResponse();
        importCountResponse.setSuccess(collect.size() - failureList.size());
        importCountResponse.setFailure(failureList.size());
        //新增导入记录表
        ImportRecord importRecord = new ImportRecord();
        importRecord.setBusinessType(BusinessTypeEnum.STUDENT.getCode());
        importRecord.setOrgId(SecurityContextHolder.getOrgId());
        importRecord.setTotal(collect.size());
        importRecord.setFailure(importCountResponse.getFailure());
        importRecord.setSuccess(importCountResponse.getSuccess());
        importRecord.setImportTime(LocalDateTime.now());
        importRecordService.save(importRecord);
        //返回值set导入记录id
        importCountResponse.setId(importRecord.getId());
        //批量新增导入记录详情表
        List<ImportRecordDetail> recordDetailList = collect.stream().map(i -> {
            ImportRecordDetail detail = new ImportRecordDetail();
            BeanUtils.copyProperties(i, detail);
            detail.setPhone(AesUtil.decode(detail.getPhone()));
            detail.setImportRecordId(importRecord.getId());
            detail.setFailureReason(CollectionUtils.isNotEmpty(i.getErrorReasons()) ? StringUtils.strip(i.getErrorReasons().toString(), "[]") : null);
            detail.setSuccess(i.getPassed() ? 1 : 0);
            return detail;
        }).collect(Collectors.toList());
        importRecordDetailService.saveBatch(recordDetailList);

        return importCountResponse;
    }





    /**
     * 加密
     *
     * @param encodeUser 需要加密的字段
     */
    private void encodeInfo(EncodeUser encodeUser) {
        if (StringUtils.isNotBlank(encodeUser.getAccount())) {
            encodeUser.setAccount(AesUtil.encode(encodeUser.getAccount()));
        }
        if (StringUtils.isNotBlank(encodeUser.getPhone())) {
            encodeUser.setPhone(AesUtil.encode(encodeUser.getPhone()));

        }
        if (StringUtils.isNotBlank(encodeUser.getIdNumber())) {
            encodeUser.setIdNumber(AesUtil.encode(encodeUser.getIdNumber()));
        }
    }

    @Override
    public IPage<StudentPageResponse> pageWithStaff(StudentPageStaffRequest params) {
        if (StringUtils.isNotBlank(params.getPhone())) {
            params.setPhone(AesUtil.encode(params.getPhone()));
        }
        if (StrUtil.isNotBlank(params.getIdNumber())) {
            params.setIdNumber(AesUtil.encode(params.getIdNumber()));
        }
        Page<?> page = params.createMpPage();
        params.setOrgId(SecurityContextHolder.getOrgId());
        if (Integer.valueOf(1).equals(params.getInner())) {
            return this.baseMapper.pageWithStaff(page, params);
        } else {
            return this.baseMapper.pageWithPure(page, params);
        }
    }

    @Override
    public List<StudentDataParseResult> importPreview(MultipartFile file) {
        /*Instant beginTime = Instant.now();
         *//*if (!FileTypeCheckUtil.isExcel(file)) {
            throw new ServiceException("文件格式有误，请检查！");
        }*//*
        Instant startTime = Instant.now();
        ExcelDataListener<StudentData> dataListener = new ExcelDataListener<>();
        try {
            EasyExcel.read(file.getInputStream(), StudentData.class, dataListener).sheet().doRead();
            //EasyExcel.read(file.getInputStream(), StudentData.class, dataListener).ignoreEmptyRow(true).sheet().doRead();
        } catch (Exception e) {
            throw new ServiceException("无法正常读取Excel表格，请检查！");
        }
        List<StudentData> dataList = dataListener.getDataList();*/
        //log.info("解析Excel耗时{}s", Duration.between(startTime, Instant.now()).getSeconds());
//        log.info("Excel导入试题解析结果: {}", dataList);

        ExcelStudentAnalysisListener dataListener = new ExcelStudentAnalysisListener();
        try {
            EasyExcel.read(file.getInputStream(), StudentExcelData.class, dataListener).ignoreEmptyRow(true)./*headRowNumber(3).*/sheet(1).doRead();
        } catch (Exception e) {
            throw new ServiceException("上传的导入模板格式不正确！");
        }
        //EasyExcel.read(file.getInputStream(), StudentExcelData.class, dataListener).sheet().doRead();
        List<StudentExcelData> dataList = dataListener.getDataList();
        if (CollectionUtils.isEmpty(dataList)){
            throw new ServiceException("解析不到数据！");
        }
        List<StudentDataParseResult> resultList = new ArrayList<>();
        List<StudentDataParseResult> errorList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            StudentExcelData studentExcelData = dataList.get(i);
            StudentDataParseResult result = new StudentDataParseResult();
            BeanUtils.copyProperties(studentExcelData, result);
            StudentDataValidate studentDataValidate = studentDataValidateFactory.getExcelStudentValidate();
            //String mark = String.format("第%d行", excelQuestion.getRowIndex());
            //excelQuestionValidate.setMark(mark);
            studentDataValidate.validate(studentExcelData);
            boolean passed = studentDataValidate.passed();
            result.setPassed(passed);
            result.setSex(Optional.ofNullable(SexEnum.getEnum(studentExcelData.getSexName())).orElse(SexEnum.OTHER).getSex());
            if (!passed) {
                result.setErrorReasons(studentDataValidate.getErrorReasons());
                errorList.add(result);
            }
            resultList.add(result);
        }

//        if (CollectionUtils.isNotEmpty(errorList)) {
//            StringBuilder errorMsg = new StringBuilder();
//            for (int j = 0; j < errorList.size(); j++) {
//                StudentDataParseResult parseResult =  errorList.get(j);
//                if (j > 0) {
//                    errorMsg.append("<br/>");
//                }
//                errorMsg.append("【第").append(parseResult.getRowNum()).append("行】");
//                for (String errorReason : parseResult.getErrorReasons()) {
//                    errorMsg.append(errorReason);
//                }
//            }
//
//            throw new IllegalParamsException(errorMsg.toString());
//        }

        //log.info("校验Excel耗时{}s，条数: {}", Duration.between(startTime, Instant.now()).getSeconds(), resultList.size());
        //log.info("解析验证Excel总耗时{}s", Duration.between(beginTime, Instant.now()).getSeconds());
        return resultList;
    }

    class ExcelStudentAnalysisListener extends AnalysisEventListener<StudentExcelData> {

        private final List<StudentExcelData> dataList = new ArrayList<>();

        /**
         * 每解析一行会回调此方法
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         *
         * @param
         * @param
         */
        @Override
        public void invoke(StudentExcelData studentExcelData, AnalysisContext analysisContext) {
            Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
            studentExcelData.setRowNum(rowIndex + 1);
            //数据存储到list，供批量处理，或后续自己业务逻辑处理。
            if (cn.qbs.wa.teach.common.core.utils.StringUtils.hasText(studentExcelData.getPhone())) {
                dataList.add(studentExcelData);
                return;
            }
            //dataList.add(studentExcelData);
            // 判断是否所有属性都为空
            Map<Integer, Cell> cellMap = analysisContext.readRowHolder().getCellMap();
            cellMap.forEach((k, v) -> {
                ReadCellData cell = (ReadCellData) v;
                if (!CellDataTypeEnum.EMPTY.equals(cell.getType())) {
                    dataList.add(studentExcelData);
                    return;
                }
            });
            /*if (!ObjUtil.checkNullAndEmpty(questionExcelVo)) {
                dataList.add(questionExcelVo);
            }*/
        }


        /**
         * 整个excel解析结束会执行此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            //log.info("所有数据解析完成！");
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
//            saveData();
        }

        List<StudentExcelData> getDataList() {
            return dataList;
        }

    }

    @Override
    public StudentDetailResponse selectDetailByIdNoTenant(IdOrgRequest request) {
        if (request.getOrgId() == null) {
            SecurityContextHolder.setOrgId(SecurityContextHolder.getSelectOrgId().toString());
        }
        return baseMapper.selectDetailByIdNoTenant(request);
    }

    @Override
    public StudentDetailResponse details(Serializable id) {
       /* StudentDetailResponse studentDetailResponse = new StudentDetailResponse();
        studentDetailResponse.setId((Long) id);
        Student student = this.getById(id);
        if (student != null) {
            BeanUtil.copyProperties(student, studentDetailResponse);
        }*/
        StudentDetailResponse studentDetailResponse=baseMapper.selectDetailById(id);
        if(studentDetailResponse==null){
            return new StudentDetailResponse();
        }
        //查询我的个人信息
        IdRequest idRequest=new IdRequest();
        //idRequest.setId(student.getUserId());
        idRequest.setId(studentDetailResponse.getUserId());
        UniMemberDTO uniMemberDTO=remoteUnionMemberService.info(idRequest).getRemoteData();
        if(uniMemberDTO!=null){
            studentDetailResponse.setBirthday(uniMemberDTO.getBirthday());
            studentDetailResponse.setWorkUnit(uniMemberDTO.getWorkUnit());
            studentDetailResponse.setHeadImgUrl(uniMemberDTO.getHeadImgUrl());
            studentDetailResponse.setEducation(uniMemberDTO.getEducation());
            studentDetailResponse.setSchool(uniMemberDTO.getSchool());
            studentDetailResponse.setNation(uniMemberDTO.getNation());
            studentDetailResponse.setNativePlace(uniMemberDTO.getNativePlace());
            studentDetailResponse.setWorkAddress(uniMemberDTO.getWorkAddress());
            studentDetailResponse.setRegisterAddress(uniMemberDTO.getRegisterAddress());
            studentDetailResponse.setResideAddress(uniMemberDTO.getResideAddress());
            //studentDetailResponse.setIdNumber(uniMemberDTO.getIdNumber());
            studentDetailResponse.setEmail(uniMemberDTO.getEmail());
        }
        // 查询部门
        List<Dept> depts = studentDeptMapper.listDeptByStu(id);
        if (!depts.isEmpty()) {
            Dept dept = depts.get(0);
            studentDetailResponse.setDeptId(dept.getId());
            studentDetailResponse.setDeptName(dept.getDeptName());
            if (StrUtil.isNotBlank(dept.getParentCode())) {
                studentDetailResponse.setDeptPidList(StrUtil.split(dept.getParentCode(), StrUtil.COMMA).stream().map(Long::parseLong).collect(Collectors.toList()));
            }
        }
        // 查询标签
        List<Groups> groups = studentGroupMapper.listGroupByStu(id);
        if (!groups.isEmpty()) {
            studentDetailResponse.setGroupIdList(groups.stream().map(Groups::getId).collect(Collectors.toList()));
            studentDetailResponse.setGroupNameList(groups.stream().map(Groups::getGroupName).collect(Collectors.toList()));
        }
        if(StringUtils.isNotEmpty(studentDetailResponse.getNation())){
            DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
            dictPageRequestDTO.setDictKey(studentDetailResponse.getNation());
            dictPageRequestDTO.setCode("nation");
            String nationName=remoteDictService.getDictValue(dictPageRequestDTO).getRemoteData();
            studentDetailResponse.setNationName(nationName);
        }
        return studentDetailResponse;
    }

    @Override
    public Long categoryCount(List<Long> categoryIdList) {
        if (categoryIdList.get(0) == -1) {
            return this.baseMapper.countNotDeptNum();
        }
        return this.studentDeptService.lambdaQuery().select(StudentDept::getStudentId)
                .in(CollectionUtils.isNotEmpty(categoryIdList), StudentDept::getDeptId, categoryIdList).count();
    }

    @Override
    public Student registerFromOtherSystem(StudentAddRequest request) {

        Long userId = request.getUserId();
        Student student = this.lambdaQuery().eq(Student::getUserId, userId).one();
        if (student != null) {
            return student;
        }

        // 远程注册统一会员账号信息
        R<UniMemberDTO> r = remoteUnionMemberService.info(new IdRequest(userId));
        if (!r.isOk()) {
            throw new ServiceException(r.getMsg());
        }
        String phone = r.getData().getAccount();
        String encodePhone = AesUtil.encode(phone);
        Long count = this.lambdaQuery().eq(Student::getAccount, encodePhone).count();
        if (count != null && count > 0) {
            throw new ServiceException(String.format("手机号：%s 存在注册记录，请联系系统管理员", phone));
        }

        encodeInfo(request);
        student = BeanUtil.copyProperties(request, Student.class);
        student.setUserId(userId);
        student.setAccount(encodePhone);
        student.setPhone(encodePhone);
        this.save(student);
        return student;
    }

    @Override
    public List<StudentExcelDTO> generateExcelData(StudentPageRequest params) {
        List<StudentDetailResponse> studentDetailResponses=new ArrayList<>();
        params.setOrgId(SecurityContextHolder.getOrgId());
        List<StudentPageResponse> studentPageResponses = this.baseMapper.page(params.createMpPage(),params).getRecords();
        for (StudentPageResponse studentPageResponse:studentPageResponses) {
            IdRequest idRequest=new IdRequest();
            idRequest.setId(studentPageResponse.getUserId());
            UniMemberDTO uniMemberDTO=remoteUnionMemberService.info(idRequest).getRemoteData();
            StudentDetailResponse studentDetailResponse=new StudentDetailResponse();
            BeanUtils.copyProperties(uniMemberDTO,studentDetailResponse);
            studentDetailResponse.setSex(studentPageResponse.getSex());
            studentDetailResponse.setRealName(studentPageResponse.getRealName());
            studentDetailResponse.setPhone(AesUtil.decode(studentPageResponse.getPhone()));
            studentDetailResponses.add(studentDetailResponse);
        }
        return convertStudentPageResponseToStudentExcelDTO(studentDetailResponses);
    }

    private List<StudentExcelDTO> convertStudentPageResponseToStudentExcelDTO(List<StudentDetailResponse> sourceData) {
        if (sourceData.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<StudentExcelDTO> targetData = new ArrayList<>(sourceData.size());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        for (StudentDetailResponse response : sourceData) {
            StudentExcelDTO studentExcelDTO = new StudentExcelDTO();
            studentExcelDTO.setRealName(response.getRealName());
            studentExcelDTO.setPhone(response.getPhone());
            if(response.getBirthday()!=null){
                studentExcelDTO.setBirthday(response.getBirthday().atStartOfDay().format(formatter));
            }
            studentExcelDTO.setEmail(response.getEmail());
            studentExcelDTO.setSchool(response.getSchool());
            studentExcelDTO.setIdNumber(response.getIdNumber());
            studentExcelDTO.setWorkUnit(response.getWorkUnit());
            studentExcelDTO.setWorkAddress(response.getWorkAddress());
            studentExcelDTO.setNativePlace(response.getNativePlace());
            if(SexEnum.MALE.getSex().equals(response.getSex())){
                studentExcelDTO.setSex(SexEnum.MALE.getName());
            }else if(SexEnum.FEMALE.getSex().equals(response.getSex())){
                studentExcelDTO.setSex(SexEnum.FEMALE.getName());
            }else {

            }
            DictPageRequestDTO dictPageRequestDTO=new DictPageRequestDTO();
            if(response.getNation()!=null){
                dictPageRequestDTO.setCode("nation");
                dictPageRequestDTO.setDictKey(response.getNation());
                String nation=remoteDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                studentExcelDTO.setNation(nation);
            }
            if(response.getEducation()!=null){
                dictPageRequestDTO.setCode("education");
                dictPageRequestDTO.setDictKey(response.getEducation());
                String education=remoteDictService.getDictValue(dictPageRequestDTO).getRemoteData();
                studentExcelDTO.setEducation(education);
            }
            targetData.add(studentExcelDTO);
        }

        return targetData;
    }

    @Override
    public List<MemberClazzResponse> pageMemberClazz(Long memberId) {
        R<List<IntegrateClazzResponse>> r = remoteTrainClazzService.listClazzByMemberId(new IdRequest(memberId));
        if (remoteValid(r)) {
            return Collections.emptyList();
        }
        return BeanUtil.copyToList(r.getData(), MemberClazzResponse.class);
    }

    @Override
    public MemberStudentResponse detailMember(IdRequest idRequest) {
        MemberStudentResponse memberStudentResponse=new MemberStudentResponse();
        // 远程查询 Member 表
        UniMemberDTO uniMemberDTO = remoteUnionMemberService.info(idRequest).getRemoteData();
        if(uniMemberDTO!=null){
            BeanUtils.copyProperties(uniMemberDTO,memberStudentResponse);
            List<String> orgNameList=baseMapper.selectOrgNameByUserId(idRequest.getId());
            memberStudentResponse.setOrgNameList(orgNameList);
        }
        return memberStudentResponse;
    }

    @Override
    public IPage<MemberResponse> pageMember(MemberPageRequest params) {
        List<UniMemberDTO> uniMemberList = new ArrayList<>();
        Page<MemberResponse> page = params.createMpPage();
        UniMemberQueryDTO uniMemberQueryDTO = new UniMemberQueryDTO();
        if (StrUtil.isNotBlank(params.getPhone()) || StrUtil.isNotBlank(params.getRealName())) {
            // 远程查询 Member 表
            uniMemberQueryDTO.setPhone(AesUtil.encode(params.getPhone()));
            uniMemberQueryDTO.setRealName(params.getRealName());
            R<PageResultComDTO<UniMemberDTO>> r = remoteUnionMemberService.query(uniMemberQueryDTO);
            if (remoteValid(r) || CollUtil.isEmpty(r.getData().getRecords())) {
                return page;
            }
            uniMemberList = r.getData().getRecords();
            page.setTotal(r.getData().getTotal());
        }

        List<Long> memberIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(uniMemberList)) {
            memberIds = uniMemberList.stream().map(UniMemberDTO::getId).collect(Collectors.toList());
        }
        if (StrUtil.isNotBlank(params.getCourseName())) {
            // 远程查询课程学员ID
            R<List<Long>> r = remoteCourseStudentService.queryStudent(new CourseStudentQueryDTO(memberIds, params.getCourseName()));
            if (remoteValid(r)) {
                return page;
            }
            memberIds = r.getData();
        }

        if (params.getOrgId() != null) {
            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
            // 本地查询机构学员ID
            List<Student> studentList = this.baseMapper.selectList(
                    Wrappers.<Student>lambdaQuery().select(Student::getUserId)
                            .in(CollUtil.isNotEmpty(memberIds), Student::getUserId, memberIds));
            if (CollUtil.isEmpty(studentList)) {
                return page;
            }
            memberIds = studentList.stream().map(Student::getUserId).collect(Collectors.toList());
        }

        if (CollUtil.isNotEmpty(uniMemberList)) {
            // 说明查询了 uni_member 无需远程查询
            // 移除多余的元素
            List<Long> finalMemberIds = memberIds;
            uniMemberList.removeIf(e -> !finalMemberIds.contains(e.getId()));
        } else {
            if (CollUtil.isNotEmpty(memberIds)) {
                memberIds = memberIds.stream().distinct().collect(Collectors.toList());
                // 根据 memberIds 查询会员用户ID
                R<List<UniMemberDTO>> r = remoteUnionMemberService.list(new IdListRequest(memberIds, null));
                if (remoteValid(r)) {
                    return page;
                }
                uniMemberList = r.getData();
            } else {
                // 默认分页查询
                uniMemberQueryDTO.setCurrent(params.getCurrent());
                uniMemberQueryDTO.setSize(params.getSize());
                R<PageResultComDTO<UniMemberDTO>> r = remoteUnionMemberService.query(uniMemberQueryDTO);
                if (remoteValid(r) || CollUtil.isEmpty(r.getData().getRecords())) {
                    return page;
                }
                uniMemberList = r.getData().getRecords();
                page.setTotal(r.getData().getTotal());
                page.setSearchCount(false);
            }
        }
        // 封装分页对象
        createPage(uniMemberList, page);
        // 查询 班级班级信息
        if (page.getTotal() > 0) {
            memberIds = page.getRecords().stream().map(MemberResponse::getMemberId).collect(Collectors.toList());
            R<Map<Long, String>> r = remoteTrainClazzStudentService.queryClazzLast(new IdListRequest(memberIds, null));
            if (r.getRemoteData() != null) {
                Map<Long, String> stuMapClazz = r.getData();
                for (MemberResponse record : page.getRecords()) {
                    record.setClazzName(stuMapClazz.get(record.getMemberId()));
                }
            }
        }
        return page;
    }

    private boolean remoteValid(R<?> r) {
        if (!r.isOk()) {
            throw new ServiceException(r.getMsg());
        }
        return ObjectUtil.isEmpty(r.getData());
    }

    /**
     * 模拟内存分页
     * @param records 分页结果
     * @param page    分页对象
     */
    private void createPage(List<UniMemberDTO> records, Page<MemberResponse> page) {
        if (CollUtil.isEmpty(records)) {
            page.setTotal(0);
            return;
        }
        List<MemberResponse> list = records.stream().map(e -> {
            MemberResponse t = new MemberResponse();
            t.setMemberId(e.getId());
            t.setSex(e.getSex());
            t.setEnabled(e.getEnabled());
            t.setPhone(e.getPhone());
            t.setRealName(e.getRealName());
            return t;
        }).collect(Collectors.toList());
        if (page.searchCount()) {
            page.setTotal(list.size());
            list = list.stream().skip((page.getCurrent() - 1) * page.getSize()).limit(page.getSize()).collect(Collectors.toList());
        }
        page.setRecords(list);
    }

}

