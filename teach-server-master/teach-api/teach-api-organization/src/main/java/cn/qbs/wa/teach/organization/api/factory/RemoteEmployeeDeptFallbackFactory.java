package cn.qbs.wa.teach.organization.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeDeptService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.EmployeeDeptListSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.EmployeeDeptTreeListDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @author Administrator
 */
public class RemoteEmployeeDeptFallbackFactory implements FallbackFactory<RemoteEmployeeDeptService> {

    @Override
    public RemoteEmployeeDeptService create(Throwable cause) {
        return new RemoteEmployeeDeptService() {
            @Override
            public R<List<EmployeeDeptTreeListDTO>> listTree(EmployeeDeptListSearchDTO params) {
                return R.fail("服务暂无法访问");
            }

            @Override
            public R<List<EmployeeDeptTreeListDTO>> list(EmployeeDeptListSearchDTO params) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
