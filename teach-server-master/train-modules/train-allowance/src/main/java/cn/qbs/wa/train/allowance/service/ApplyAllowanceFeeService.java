package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplyAllowanceFee;
import cn.qbs.wa.train.allowance.pojo.applyallowancefee.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请明细-网络安全培训费用(ApplyAllowanceFee)表服务接口
 *
 * @author makejava
 * @since 2022-11-03 19:43:28
 */
public interface ApplyAllowanceFeeService extends IService<ApplyAllowanceFee> {

    /**
     * 新增资助资金申请明细-网络安全培训费用
     * @param params
     * @return
     */
    boolean add(ApplyAllowanceFeeAddRequest params);

    /**
     * 分页查询资助资金申请明细-网络安全培训费用
     * @param params
     * @return
     */
    IPage<ApplyAllowanceFeePageResponse> page(ApplyAllowanceFeePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    ApplyAllowanceFeeDetailResponse detail(Serializable id);

    /**
     * 更新资助资金申请明细-网络安全培训费用
     * @param params
     * @return
     */
    boolean update(ApplyAllowanceFeeUpdateRequest params);

    /**
     * 删除资助资金申请明细-网络安全培训费用
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    void copyByApplyId(Long sourceAllowanceId, Long targetAllowanceId);
}

