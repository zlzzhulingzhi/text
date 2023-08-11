package cn.qbs.wa.teach.cert.service;

import cn.qbs.wa.teach.cert.entity.AwardRecordBusiness;
import cn.qbs.wa.teach.cert.pojo.awardrecordbusiness.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 基础直播业务关联表(AwardRecordBusiness)表服务接口
 *
 * @author makejava
 * @since 2022-01-19 11:38:18
 */
public interface AwardRecordBusinessService extends IService<AwardRecordBusiness> {

    /**
     * 新增基础直播业务关联表
     * @param params
     * @return
     */
    boolean add(AwardRecordBusinessAddRequest params);

    /**
     * 分页查询基础直播业务关联表
     * @param params
     * @return
     */
    IPage<AwardRecordBusinessPageResponse> page(AwardRecordBusinessPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    AwardRecordBusinessDetailResponse detail(Serializable id);

    /**
     * 更新基础直播业务关联表
     * @param params
     * @return
     */
    boolean update(AwardRecordBusinessUpdateRequest params);

    /**
     * 删除基础直播业务关联表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

