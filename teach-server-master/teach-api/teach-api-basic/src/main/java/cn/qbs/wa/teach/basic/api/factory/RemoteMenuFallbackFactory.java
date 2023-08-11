package cn.qbs.wa.teach.basic.api.factory;

import cn.qbs.wa.teach.basic.api.RemoteMenuService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.menu.MenuListResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteMenuFallbackFactory implements FallbackFactory<RemoteMenuService> {


    @Override
    public RemoteMenuService create(Throwable cause) {
        return new RemoteMenuService() {
            @Override
            public R<List<Long>> getMenuIds() {
                return null;
            }

            @Override
            public R<List<MenuListResultDTO>> listMenu(MenuListDTO menuListDTO) {
                return null;
            }
        };
    }
}

