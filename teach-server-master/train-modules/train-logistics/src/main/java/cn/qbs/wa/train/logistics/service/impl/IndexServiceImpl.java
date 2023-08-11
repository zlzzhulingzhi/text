package cn.qbs.wa.train.logistics.service.impl;

import cn.qbs.wa.train.logistics.mapper.IndexMapper;
import cn.qbs.wa.train.logistics.pojo.index.OrgInfoResponse;
import cn.qbs.wa.train.logistics.service.IndexService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yjx
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Resource
    private IndexMapper indexMapper;


    @Override
    public List<OrgInfoResponse> listOrg(Long userId) {
        return indexMapper.listOrg(userId);
    }
}
