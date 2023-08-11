package cn.qbs.wa.teach.out.union.admin.api;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.out.union.admin.api.DTO.*;
import cn.qbs.wa.teach.out.union.admin.api.factory.RemoteUnionMemberFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "remoteUnionMemberService", name = "union-admin", path = "u-admin/uniMember", fallbackFactory = RemoteUnionMemberFallbackFactory.class)
public interface RemoteUnionMemberService {

    @PostMapping("/inner/register")
    R<UniMemberDTO> register(@RequestBody UniMemberAddDTO params);

    @PostMapping("/inner/info")
    R<UniMemberDTO> info(@RequestBody IdRequest idRequest);

    @PostMapping("/inner/list")
    R<List<UniMemberDTO>> list(@RequestBody IdListRequest request);

    @PostMapping("/inner/getByNameOrPhone")
    R<List<UniMemberDTO>> getByNameOrPhone(@RequestBody UniMemberTCourseStudentRequestDTO params);

    @PostMapping("/inner/getCount")
    R<Long> getCount(@RequestBody UniMemberPageRequestDTO request);

    @PostMapping("/inner/query")
    R<PageResultComDTO<UniMemberDTO>> query(@RequestBody UniMemberQueryDTO uniMemberQueryDTO);
}
