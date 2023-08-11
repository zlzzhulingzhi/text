package cn.qbs.wa.train.logistics.api.factory;


import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.api.RemoteTrainLogisticsEmployeeService;
import cn.qbs.wa.train.logistics.api.RemoteTrainLogisticsService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.LoginInfoDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.EmployeeAddRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.EmployeeDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.EmployeeUpdateRequestDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.employee.UpdateBindUserDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author Administrator
 */
public class RemoteTrainLogisticsEmployeeFallbackFactory implements FallbackFactory<RemoteTrainLogisticsEmployeeService> {


    @Override
    public RemoteTrainLogisticsEmployeeService create(Throwable cause) {
        return new RemoteTrainLogisticsEmployeeService() {


            @Override
            public R<EmployeeDTO> add(EmployeeAddRequestDTO params) {
                return null;
            }

            @Override
            public R adminUpdate(EmployeeUpdateRequestDTO params) {
                return null;
            }

            @Override
            public R<LoginInfoDTO> getLoginInfo(IdRequest request) {
                return null;
            }

            @Override
            public R<Boolean> updateBindUser(UpdateBindUserDTO requestDTO) {
                return null;
            }
        };
    }
}
