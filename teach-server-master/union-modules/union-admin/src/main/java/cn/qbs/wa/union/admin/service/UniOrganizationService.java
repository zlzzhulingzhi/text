package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.UniOrganization;
import cn.qbs.wa.union.admin.pojo.uniorganization.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 统一机构(UniOrganization)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-12 15:44:59
 */
public interface UniOrganizationService extends IService<UniOrganization> {

    /**
     * 新增统一机构
     * @param params
     * @return
     */
    boolean add(UniOrganizationAddRequest params);

    /**
     * 分页查询统一机构
     * @param params
     * @return
     */
    IPage<UniOrganizationPageResponse> page(UniOrganizationPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    UniOrganizationDetailResponse detail(Serializable id);

    /**
     * 更新统一机构
     * @param params
     * @return
     */
    boolean update(UniOrganizationUpdateRequest params);

    /**
     * 删除统一机构
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    void batchEnabled(Integer flag, List<Long> idList);
}

