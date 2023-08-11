package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.*;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Administrator
 */
@Component
public class RemoteEmployeeFallbackFactory implements FallbackFactory<RemoteEmployeeService> {


    @Override
    public RemoteEmployeeService create(Throwable cause) {
        return new RemoteEmployeeService() {
            @Override
            public R<EmployeeDTO> add(OrganizationEmployeeAddRequestDTO params) {
                return null;
            }

            @Override
            public R<List<EmployeeListResultDTO>> adminList(EmployeeListSearchDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<EmployeeListResultDTO>> list(EmployeeListSearchDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<EmployeeListFullResultDTO>> listFull(EmployeeListSearchDTO params) {
               return R.fail("服务暂无法访问");
            }

            @Override
            public R<EmployeeDTO> register(EmployeeRegisterDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<PageResultComDTO<EmployeePageResultDTO>> page(EmployeePageSearchDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<EmployeeDTO> getLoginInfo(IdRequest request) {
                return null;
            }

            @Override
            public R<Boolean> updateBindUser(UpdateBindUserDTO requestDTO) {
                return null;
            }
        };
    }
}
