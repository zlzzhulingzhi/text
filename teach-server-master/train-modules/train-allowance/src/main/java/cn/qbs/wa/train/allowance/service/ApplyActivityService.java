package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplyActivity;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyRequest;
import cn.qbs.wa.train.allowance.pojo.applyactivity.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请-学术会议和竞赛活动(ApplyActivity)表服务接口
 *
 * @author makejava
 * @since 2022-11-03 19:27:14
 */
public interface ApplyActivityService extends IService<ApplyActivity> {

    /**
     * 新增资助资金申请-学术会议和竞赛活动
     *
     * @param params
     * @return
     */
    boolean add(ApplyActivityAddRequest params);
    

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ApplyActivityDetailResponse detail(Serializable id);

    /**
     * 更新资助资金申请-学术会议和竞赛活动
     *
     * @param params
     * @return
     */
    boolean update(ApplyActivityUpdateRequest params);

    /**
     * 删除资助资金申请-学术会议和竞赛活动
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    Long saveActivityApply(ApplyActivitySaveRequest params);

    void checkCompleteness(Long id);

    IPage<ApplyPageResponse> page(ApplyActivityPageRequest request, ApplyRequest applyRequest);

    boolean copyActivityApply(Long applyId);
}

