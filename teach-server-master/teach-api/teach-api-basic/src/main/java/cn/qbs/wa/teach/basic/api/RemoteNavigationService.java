package cn.qbs.wa.teach.basic.api;

import cn.qbs.wa.teach.basic.api.factory.RemoteNavigationFallbackFactory;
import cn.qbs.wa.teach.basic.api.pojo.DTO.navigation.NavigationItemDTO;
import cn.qbs.wa.teach.common.core.domain.R;
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
@FeignClient(contextId = "remoteNavigationService", name = "teach-basic", path = "basic/navigation", fallbackFactory = RemoteNavigationFallbackFactory.class)
public interface RemoteNavigationService {

    /**
     * 导航链接列表
     * @param navigationItemDTO 查询参数
     * @return 导航链接列表
     */
    @PostMapping("/list")
    R<List<NavigationItemDTO>> list(@RequestBody NavigationItemDTO navigationItemDTO);

}
