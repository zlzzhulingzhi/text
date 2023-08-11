package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.common.entity.TCertRule;
import cn.qbs.wa.teach.exam.admin.pojo.tcertrule.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 证书规则表(TCertRule)表服务接口
 *
 * @author makejava
 * @since 2022-05-16 13:49:20
 */
public interface TCertRuleService extends IService<TCertRule> {

    /**
     * 新增证书规则表
     * @param params
     * @return
     */
    boolean add(TCertRuleAddRequest params);

    /**
     * 分页查询证书规则表
     * @param params
     * @return
     */
    IPage<TCertRulePageResponse> page(TCertRulePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    TCertRuleDetailResponse detail(Serializable id);

    /**
     * 更新证书规则表
     * @param params
     * @return
     */
    boolean update(TCertRuleUpdateRequest params);

    /**
     * 删除证书规则表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);
    
}

