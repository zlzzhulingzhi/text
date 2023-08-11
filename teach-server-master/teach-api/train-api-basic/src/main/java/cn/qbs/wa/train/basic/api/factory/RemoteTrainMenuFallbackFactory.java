package cn.qbs.wa.train.basic.api.factory;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.api.pojo.DTO.menu.MenuListDTO;
import cn.qbs.wa.train.basic.api.pojo.DTO.menu.MenuListResultDTO;
import cn.qbs.wa.train.basic.api.RemoteTrainMenuService;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteTrainMenuFallbackFactory implements FallbackFactory<RemoteTrainMenuService> {


    @Override
    public RemoteTrainMenuService create(Throwable cause) {
        return new RemoteTrainMenuService() {

            @Override
            public R<List<MenuListResultDTO>> listAllMenu() {
                return null;
            }

            @Override
            public R<List<MenuListResultDTO>> listMenu(MenuListDTO menuListDTO) {
                return null;
            }

            @Override
            public R<List<Long>> getMenuIds() {
                return null;
            }
        };
    }
}

