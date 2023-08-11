package cn.qbs.wa.train.logistics.service;


import cn.qbs.wa.train.logistics.pojo.index.OrgInfoResponse;

import java.util.List;

/**
 * @author yjx
 */
public interface IndexService {

    List<OrgInfoResponse> listOrg(Long userId);

}
