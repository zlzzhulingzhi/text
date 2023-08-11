package cn.qbs.wa.teach.cert.service;

import cn.qbs.wa.teach.cert.entity.StandardTemplateConfig;
import cn.qbs.wa.teach.cert.pojo.standardtemplateconfig.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 证书模板配置(StandardTemplateConfig)表服务接口
 *
 * @author makejava
 * @since 2022-01-19 11:38:22
 */
public interface StandardTemplateConfigService extends IService<StandardTemplateConfig> {

    /**
     * 新增证书模板配置
     * @param params
     * @return
     */
    boolean add(StandardTemplateConfigAddRequest params);

    /**
     * 分页查询证书模板配置
     * @param params
     * @return
     */
    IPage<StandardTemplateConfigPageResponse> page(StandardTemplateConfigPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    StandardTemplateConfigDetailResponse detail(Serializable id);

    /**
     * 更新证书模板配置
     * @param params
     * @return
     */
    boolean update(StandardTemplateConfigUpdateRequest params);

    /**
     * 删除证书模板配置
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

