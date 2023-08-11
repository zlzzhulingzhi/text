package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplyAllowance;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyAllowancePageRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyAllowanceSaveRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyDetailByAllowanceResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyUpdateByAllowanceRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请-网络安全培训课程(ApplyAllowance)表服务接口
 *
 * @author makejava
 * @since 2022-11-03 19:28:56
 */
public interface ApplyAllowanceService extends IService<ApplyAllowance> {

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ApplyDetailByAllowanceResponse detail(Serializable id);

    /**
     * 更新资助资金申请-网络安全培训课程
     *
     * @param params
     * @return
     */
    boolean update(ApplyUpdateByAllowanceRequest params);

    /**
     * 删除资助资金申请-网络安全培训课程
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    Long saveAllowanceApply(ApplyAllowanceSaveRequest params);

    void checkCompleteness(Long id);

    IPage<ApplyPageResponse> page(ApplyAllowancePageRequest request, ApplyRequest applyRequest);

    boolean copyAllowanceApply(Long applyId);
}

