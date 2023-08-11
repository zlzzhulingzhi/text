package cn.qbs.wa.train.basic.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.api.factory.RemoteTrainMenuFallbackFactory;
import cn.qbs.wa.train.basic.api.pojo.DTO.menu.MenuListDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.menu.MenuListResultDTO;
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
@FeignClient(contextId = "remoteTrainMenuService", name="train-basic", path = "/train/basic",fallbackFactory = RemoteTrainMenuFallbackFactory.class)
public interface RemoteTrainMenuService {

    @PostMapping("/inner/menu/org-menu")
    @ApiOperation("查询机构所有菜单")
    R<List<MenuListResultDTO>> listAllMenu();

    @PostMapping("/menu/list")
    @ApiOperation("列表查询所有菜单")
    R<List<MenuListResultDTO>> listMenu(@RequestBody MenuListDTO menuListDTO);

    @PostMapping("/inner/role/getMenuIds")
    @ApiOperation("获取菜单id")
    R<List<Long>> getMenuIds();


}
