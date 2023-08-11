package cn.qbs.wa.teach.basic.api;

import cn.qbs.wa.teach.basic.api.factory.RemoteMenuFallbackFactory;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteMenuService", name="teach-basic", path = "basic",fallbackFactory = RemoteMenuFallbackFactory.class)
public interface RemoteMenuService {

    @PostMapping("/inner/role/getMenuIds")
    @ApiOperation("获取菜单id")
    R<List<Long>> getMenuIds();

    @PostMapping("/menu/list")
    @ApiOperation("列表查询所有菜单")
    R<List<MenuListResultDTO>> listMenu(@RequestBody MenuListDTO menuListDTO) ;


}
