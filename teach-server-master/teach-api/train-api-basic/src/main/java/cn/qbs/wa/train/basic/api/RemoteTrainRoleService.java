package cn.qbs.wa.train.basic.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.api.factory.RemoteTrainRoleFallbackFactory;
import cn.qbs.wa.train.basic.api.pojo.DTO.role.RoleDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.role.RoleRequestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteTrainRoleService", name = "train-basic", path = "/train/basic/inner",
        fallbackFactory = RemoteTrainRoleFallbackFactory.class)
public interface RemoteTrainRoleService {


    @PostMapping("/role/getRole")
    @ApiOperation("获取角色信息")
    R<RoleDTO> getRole(@RequestBody RoleRequestDTO roleRequestDTO);

    @PostMapping("/role/getRoles")
    @ApiOperation("获取角色信息集合")
    R<List<RoleDTO>> getRoles(@RequestBody RoleRequestDTO roleRequestDTO);

}
