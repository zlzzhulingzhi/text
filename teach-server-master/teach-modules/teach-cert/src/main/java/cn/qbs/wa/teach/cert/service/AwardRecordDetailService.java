package cn.qbs.wa.teach.cert.service;

import cn.qbs.wa.teach.cert.entity.AwardRecordDetail;
import cn.qbs.wa.teach.cert.pojo.awardrecorddetail.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 证书配置(AwardRecordDetail)表服务接口
 *
 * @author makejava
 * @since 2022-01-19 11:38:19
 */
public interface AwardRecordDetailService extends IService<AwardRecordDetail> {

    /**
     * 新增证书配置
     * @param params
     * @return
     */
    boolean add(AwardRecordDetailAddRequest params);

    /**
     * 分页查询证书配置
     * @param params
     * @return
     */
    IPage<AwardRecordDetailPageResponse> page(AwardRecordDetailPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    AwardRecordDetailDetailResponse detail(Serializable id);

    /**
     * 更新证书配置
     * @param params
     * @return
     */
    boolean update(AwardRecordDetailUpdateRequest params);

    /**
     * 删除证书配置
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

