package cn.qbs.wa.teach.organization.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.api.factory.RemoteGroupsFallbackFactory;
import cn.qbs.wa.teach.organization.api.pojo.DTO.groups.GroupsDetailResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Wx
 * 机构后台优惠券所使用的远程接口
 */
@FeignClient(contextId = "remoteGroupsService", name = "teach-org", path = "org/groups", fallbackFactory = RemoteGroupsFallbackFactory.class)
public interface RemoteGroupsService {
    @ApiOperation("查询所有启用标签")
    @PostMapping("list")
    R<List<GroupsDetailResponseDTO>> list();

    /**
     * 批量查询标签详情
     */
    @ApiOperation("根据标签id批量查询标签详情")
    @PostMapping("detailList")
    R<List<GroupsDetailResponseDTO>> detailList(@RequestBody IdListRequest request);
}

