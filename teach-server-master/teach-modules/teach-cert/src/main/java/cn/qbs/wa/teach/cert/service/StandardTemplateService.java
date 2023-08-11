package cn.qbs.wa.teach.cert.service;

import cn.qbs.wa.teach.cert.entity.StandardTemplate;
import cn.qbs.wa.teach.cert.pojo.standardtemplate.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 证书模板(StandardTemplate)表服务接口
 *
 * @author makejava
 * @since 2022-01-19 11:38:21
 */
public interface StandardTemplateService extends IService<StandardTemplate> {

    /**
     * 新增证书模板
     *
     * @param params
     * @return
     */
    boolean add(StandardTemplateAddRequest params);

    /**
     * 分页查询证书模板
     *
     * @param params
     * @return
     */
    IPage<StandardTemplatePageResponse> page(StandardTemplatePageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    StandardTemplateDetailResponse detail(Serializable id);

    /**
     * 更新证书模板
     *
     * @param params
     * @return
     */
    boolean update(StandardTemplateUpdateRequest params);

    /**
     * 删除证书模板
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    void batchEnabled(Integer flag, List<Long> idList);

    Boolean getCheckCert(StandardTemplateCheckCertRequest request);

    StandardTemplateDetailSearchConfigResponse detailSearchConfig(Long id);

    void updateSearchConfig(StandardTemplateUpdateSearchConfigRequest params);

    StandardTemplateDetailResponse preview(Long id);
}

