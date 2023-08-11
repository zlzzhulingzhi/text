package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.qbs.wa.teach.common.core.config.RSAConfig;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberAddDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import cn.qbs.wa.train.logistics.entity.*;
import cn.qbs.wa.train.logistics.enums.AccountTypeEnum;
import cn.qbs.wa.train.logistics.enums.SexEnum;
import cn.qbs.wa.train.logistics.enums.SocialPlatformEnum;
import cn.qbs.wa.train.logistics.excel.StudentDataParseResult;
import cn.qbs.wa.train.logistics.excel.StudentDataValidate;
import cn.qbs.wa.train.logistics.excel.StudentDataValidateFactory;
import cn.qbs.wa.train.logistics.excel.StudentExcelData;
import cn.qbs.wa.train.logistics.mapper.*;
import cn.qbs.wa.train.logistics.pojo.appoauth.AppOAuthUserRequest;
import cn.qbs.wa.train.logistics.pojo.employee.PersonalPasswordUpdateRequest;
import cn.qbs.wa.train.logistics.pojo.employee.PersonalPhoneUpdateRequest;
import cn.qbs.wa.train.logistics.pojo.groups.GroupsDetailResponse;
import cn.qbs.wa.train.logistics.pojo.student.*;
import cn.qbs.wa.train.logistics.pojo.studentdept.StudentDeptSingleAddRequest;
import cn.qbs.wa.train.logistics.pojo.studentgroup.StudentGroupAddRequest;
import cn.qbs.wa.train.logistics.service.AppOauthService;
import cn.qbs.wa.train.logistics.service.StudentDeptService;
import cn.qbs.wa.train.logistics.service.StudentGroupService;
import cn.qbs.wa.train.logistics.service.StudentService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
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
    private StudentDeptMapper studentDeptMapper;

    @Resource
    private StudentGroupMapper studentGroupMapper;

    @Resource
    private RemoteUnionMemberService remoteUnionMemberService;

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
        StudentDetailResponse studentDetailResponse = new StudentDetailResponse();
        studentDetailResponse.setId((Long) id);
        Student student = this.getById(id);
        if (student != null) {
            BeanUtil.copyProperties(student, studentDetailResponse);
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
}

