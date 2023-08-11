package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplySettle;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyUpdateBySettleRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 入驻申请(ApplySettle)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
public interface ApplySettleService extends IService<ApplySettle> {

    boolean copySettleApply(Long applyId);

    boolean update(ApplyUpdateBySettleRequest params);

    void checkCompleteness(Long applyId);

    void checkSettleOccupy(Long applyId);
}

