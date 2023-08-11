package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;

import java.util.List;
import java.util.Map;

public interface RemoteService {
    /**
     * 远程调用获取学员用户信息List
     * @param memberIds 学员用户id数组
     * @return 学员户信息
     */
    List<UniMemberDTO> remoteMemberList(List<Long> memberIds);

    /**
     * 远程调用获取学员用户信息Map
     * @param memberIds 学员用户id数组
     * @return 用户信息
     */
    Map<Long, UniMemberDTO> remoteMemberMap(List<Long> memberIds);
}
