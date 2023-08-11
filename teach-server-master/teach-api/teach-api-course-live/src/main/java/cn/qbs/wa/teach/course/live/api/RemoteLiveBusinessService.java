package cn.qbs.wa.teach.course.live.api;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.live.api.factory.RemoteLiveBusinessFallbackFactory;
import cn.qbs.wa.teach.course.live.api.pojo.DTO.livebusiness.LiveBusinessUpdateDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 直播业务关系管理
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteLiveBusinessService", name = "teach-course-live", path = "/course-live-admin/basicLiveBusiness", fallbackFactory = RemoteLiveBusinessFallbackFactory.class)
public interface RemoteLiveBusinessService {


    @PostMapping("update")
    @ApiOperation("更新或新增基础直播业务关联表")
    R update(@RequestBody LiveBusinessUpdateDTO liveBusinessUpdateDTO);

}
