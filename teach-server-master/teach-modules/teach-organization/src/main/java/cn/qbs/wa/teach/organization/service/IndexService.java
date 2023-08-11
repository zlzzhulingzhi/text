package cn.qbs.wa.teach.organization.service;


import cn.qbs.wa.teach.organization.pojo.index.OrgInfoResponse;

import java.util.List;

/**
 * @author yjx
 */
public interface IndexService {

    List<OrgInfoResponse> listOrg(Long userId);

}
