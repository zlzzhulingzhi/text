package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.*;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yjx
 */
@Component
public class RemoteStudentFallbackFactory implements FallbackFactory<RemoteStudentService> {
    @Override
    public RemoteStudentService create(Throwable cause) {
        return new RemoteStudentService() {
            @Override
            public R<LoginUser> register(StudentDTO studentDTO) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<Boolean> resetPassword(StudentPasswordDTO passwordDTO) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<LoginUser> getLoginInfo(LoginInfoDTO loginInfoDTO) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<LoginUser> getSocialLoginInfo(SocialLoginInfoDTO socialLoginInfoDTO) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<StudentDTO> getSocialLoginInfos(SocialLoginInfoDTO socialLoginInfoDTO) {
                return null;
            }

            @Override
            public R<StudentDTO> add(TCourseStudentAddRequestDTO tCourseStudentAddRequest) {
                return null;
            }

            @Override
            public R<LoginUser> bindingSocial(SocialBindingDTO socialBindingDTO) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<Boolean> getPhoneBindingInfo(LoginInfoDTO loginInfoDTO) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<PageResultComDTO<StudentDTO>> pageWithStaff(StudentSearchDTO searchDTO) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<PageResultComDTO<StudentDTO>> pageNoTenant(StudentSearchDTO searchDTO) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<StudentDTO>> list(StudentSearchDTO searchDTO) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<StudentDTO> detail(IdOrgRequest idRequest) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<StudentDTO> detailNoTenant(IdOrgRequest request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<StudentDTO> details(IdOrgRequest request) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<StudentDTO> registerFromInner(StudentDTO request) {
                return null;
            }

            @Override
            public R<LoginUser> loginByUserID(IdOrgRequest idRequest) {
                return null;
            }

            @Override
            public R<StudentDetailResponseDTO> getStudentName(StudentDTO request) {
                return null;
            }

            @Override
            public R<List<StudentDTO>> getStu(StudentDTO request) {
                return null;
            }
        };
    }
}
