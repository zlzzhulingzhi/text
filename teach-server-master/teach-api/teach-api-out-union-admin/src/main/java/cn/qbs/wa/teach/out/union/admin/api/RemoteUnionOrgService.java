package cn.qbs.wa.teach.out.union.admin.api;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniOrgInfoResponseDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniOrgSearchDTO;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "remoteUnionOrgService", name = "union-admin", path = "u-admin/uniOrg/inner")
public interface RemoteUnionOrgService {

    @PostMapping("/page")
    R<PageResultComDTO<UniOrgInfoResponseDTO>> page(@RequestBody UniOrgSearchDTO params);

    @PostMapping("/list")
    R<List<UniOrgInfoResponseDTO>> list(@RequestBody UniOrgSearchDTO params);

    @PostMapping("/info")
    R<UniOrgInfoResponseDTO> info(@RequestBody IdRequest idRequest);

    @PostMapping("/changeToPlan")
    R<Boolean> becomeToPlan(@RequestBody IdRequest idRequest);
}
