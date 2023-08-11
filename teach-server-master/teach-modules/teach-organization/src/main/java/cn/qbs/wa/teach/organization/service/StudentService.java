package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.organization.entity.Student;
import cn.qbs.wa.teach.organization.excel.StudentDataParseResult;
import cn.qbs.wa.teach.organization.excel.StudentExcelDTO;
import cn.qbs.wa.teach.organization.pojo.employee.PersonalPasswordUpdateRequest;
import cn.qbs.wa.teach.organization.pojo.employee.PersonalPhoneUpdateRequest;
import cn.qbs.wa.teach.organization.pojo.importrecord.ImportCountResponse;
import cn.qbs.wa.teach.organization.pojo.student.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * 学员(Student)表服务接口
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
public interface StudentService extends IService<Student> {

    /**
     * 新增学员
     * @param params
     * @return
     */
    LoginUser add(StudentAddRequest params);

    /**
     * 分页查询学员
     * @param params
     * @return
     */
    IPage<StudentPageResponse> page(StudentPageRequest params);

    /**
     * 分页查询学员
     * @param params
     * @return
     */
    IPage<StudentPageResponse> pageV2(StudentPageRequest params);

    IPage<StudentPageResponse> pageNoTenant(StudentPageRequest params);

    List<StudentListResponse> list(StudentListRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    StudentDetailResponse detail(Serializable id);

    /**
     * 更新学员
     * @param params
     * @return
     */
    boolean update(StudentUpdateRequest params);

    /**
     * 删除学员
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    LoginUser getLoginInfo(LoginInfoRequest loginInfoRequest);

    LoginUser getLoginInfoFromSocial(SocialLoginInfoRequest request);

    LoginUser bindingSocial(SocialBindingRequest request);

    Boolean getPhoneBindingInfo(LoginInfoRequest request);

    Boolean unbindSocial(Long studentId);

    boolean changePhone(PersonalPhoneUpdateRequest params);

    boolean changePassword(PersonalPasswordUpdateRequest params);

    boolean resetPassword(PasswordResetRequest params);

    ImportCountResponse batchSave(List<StudentAddRequest> collect);

    IPage<StudentPageResponse> pageWithStaff(StudentPageStaffRequest params);

    List<StudentDataParseResult> importPreview(MultipartFile file);

    StudentDetailResponse selectDetailByIdNoTenant(IdOrgRequest request);

    StudentDetailResponse details(Serializable id);

    Long categoryCount(List<Long> categoryIdList);

    Student registerFromOtherSystem(StudentAddRequest request);

    LoginUser getLoginInfoByUserID(Long userId);

    IPage<StudentToClazz> getOrgList(StudentPageRequest params);

    IPage<StudentToClazz> getClazzOrgList(StudentPageRequest params);

    List<StudentExcelDTO> generateExcelData(StudentPageRequest request);

    /**
     * 机构学员综合查询
     */
    IPage<MemberResponse> pageMember(MemberPageRequest params);

    /**
     * 机构学员所在班级
     */
    List<MemberClazzResponse> pageMemberClazz(Long memberId);

    /**
     * 机构学员学员详情
     */
    MemberStudentResponse detailMember(IdRequest idRequest);
}

