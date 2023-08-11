package cn.qbs.wa.train.basic.api.factory;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.api.RemoteTrainRoleService;
import cn.qbs.wa.train.basic.api.pojo.DTO.role.RoleDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.role.RoleRequestDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteTrainRoleFallbackFactory implements FallbackFactory<RemoteTrainRoleService> {


    @Override
    public RemoteTrainRoleService create(Throwable cause) {
        return new RemoteTrainRoleService() {

            @Override
            public R<RoleDTO> getRole(RoleRequestDTO roleRequestDTO) {
                return null;
            }

            @Override
            public R<List<RoleDTO>> getRoles(RoleRequestDTO roleRequestDTO) {
                return null;
            }
        };
    }
}

