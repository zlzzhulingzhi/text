package cn.qbs.wa.teach.basic.api.factory;

import cn.qbs.wa.teach.basic.api.RemoteNavigationService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.navigation.NavigationItemDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @author yjx
 */
public class RemoteNavigationFallbackFactory implements FallbackFactory<RemoteNavigationService> {
    @Override
    public RemoteNavigationService create(Throwable cause) {
        return new RemoteNavigationService() {
            @Override
            public R<List<NavigationItemDTO>> list(NavigationItemDTO navigationDTO) {
                return R.fail("服务暂无法访问");
            }
        };
    }
}
