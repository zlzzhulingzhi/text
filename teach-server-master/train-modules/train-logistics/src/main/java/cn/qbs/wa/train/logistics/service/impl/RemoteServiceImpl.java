package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import cn.qbs.wa.train.logistics.service.RemoteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("remoteService")
public class RemoteServiceImpl implements RemoteService {

    @Resource
    private RemoteUnionMemberService remoteUnionMemberService;

    @Override
    public List<UniMemberDTO> remoteMemberList(List<Long> memberIds) {
        return remoteUnionMemberService.list(new IdListRequest(memberIds, null)).getRemoteData();
    }

    @Override
    public Map<Long, UniMemberDTO> remoteMemberMap(List<Long> memberIds) {
        List<UniMemberDTO> memberList = remoteMemberList(memberIds);
        return CollUtil.isEmpty(memberList) ? Collections.emptyMap() : memberList.stream().collect(Collectors.toMap(UniMemberDTO::getId, Function.identity()));
    }
}
