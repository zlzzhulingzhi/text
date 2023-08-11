package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.organization.api.factory.RemoteStudentFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Administrator
 */
@FeignClient(contextId = "remoteStudentService", name = "teach-org", path = "org/student", fallbackFactory = RemoteStudentFallbackFactory.class)
public interface RemoteStudentService {

    @PostMapping("/login-info")
    R<LoginUser> getLoginInfo(@RequestBody LoginInfoDTO loginInfoDTO);

    @PostMapping("/login-social")
    R<LoginUser> getSocialLoginInfo(@RequestBody SocialLoginInfoDTO socialLoginInfoDTO);

    @PostMapping("/binding-social")
    R<LoginUser> bindingSocial(@RequestBody SocialBindingDTO socialBindingDTO);

    @PostMapping("/binding-check")
    R<Boolean> getPhoneBindingInfo(@RequestBody LoginInfoDTO loginInfoDTO);

    @PostMapping("/add")
    R<LoginUser> register(@RequestBody StudentDTO studentDTO);

    @PostMapping("/reset-password")
    R<Boolean> resetPassword(@RequestBody StudentPasswordDTO passwordDTO);

    @PostMapping("/staff/page")
    R<PageResultComDTO<StudentDTO>> pageWithStaff(@RequestBody StudentSearchDTO searchDTO);

    @PostMapping("/admin/page")
    R<PageResultComDTO<StudentDTO>> pageNoTenant(@RequestBody StudentSearchDTO searchDTO);

    @PostMapping("/list")
    R<List<StudentDTO>> list(@RequestBody StudentSearchDTO searchDTO);

    @PostMapping("/detail")
    R<StudentDTO> detail(@RequestBody IdOrgRequest idRequest);

    /**
     * 查询学员详情,异步查询
     *
     * @param
     * @return
     */
    @PostMapping("/detailNoTenant")
    R<StudentDTO> detailNoTenant(@RequestBody IdOrgRequest request);

    @ApiOperation("根据ID查询学员详情,标签,部门")
    @PostMapping("/details")
    R<StudentDTO> details(@RequestBody @Validated IdOrgRequest request);

    @PostMapping("/inner/register")
    R<StudentDTO> registerFromInner(@RequestBody StudentDTO request);

    @PostMapping("/inner/loginByUserID")
    R<LoginUser> loginByUserID(@RequestBody IdOrgRequest idRequest);

    @PostMapping("inner/getStudentName")
    R<StudentDetailResponseDTO> getStudentName(StudentDTO request);

    @PostMapping("inner/getStu")
    R<List<StudentDTO>> getStu(StudentDTO request);

    @PostMapping("inner/login-socials")
    R<StudentDTO> getSocialLoginInfos(SocialLoginInfoDTO socialLoginInfoDTO);

    @PostMapping("inner/add")
    R<StudentDTO> add(TCourseStudentAddRequestDTO tCourseStudentAddRequest);
}
