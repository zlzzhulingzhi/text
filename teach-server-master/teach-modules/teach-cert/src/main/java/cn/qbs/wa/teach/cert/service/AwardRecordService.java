package cn.qbs.wa.teach.cert.service;

import cn.qbs.wa.teach.cert.entity.AwardRecord;
import cn.qbs.wa.teach.cert.pojo.awardrecord.*;
import cn.qbs.wa.teach.cert.pojo.cert.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 颁发记录(AwardRecord)表服务接口
 *
 * @author makejava
 * @since 2022-01-19 11:38:17
 */
public interface AwardRecordService extends IService<AwardRecord> {

    /**
     * 新增颁发记录
     * @param params
     * @return
     */
    boolean add(AwardRecordAddRequest params);

    /**
     * 分页查询颁发记录
     * @param params
     * @return
     */
    IPage<AwardRecordPageResponse> page(AwardRecordPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    AwardRecordDetailResponse detail(Serializable id);

    /**
     * 更新颁发记录
     * @param params
     * @return
     */
    boolean update(AwardRecordUpdateRequest params);

    /**
     * 删除颁发记录
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<AwardRecordCountResponse> listAwardCount(List<Long> certIdList);


    IPage<AwardRecordPageResponse> selectConditionPage(AwardRecordPageRequest recordPageRequest);

    List<AwardRecord> selectConditionList(AwardRecordListRequest awardRecordListRequest);

}

