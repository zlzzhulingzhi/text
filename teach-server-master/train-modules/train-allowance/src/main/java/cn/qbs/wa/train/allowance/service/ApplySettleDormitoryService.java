package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplySettleDormitory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * 入驻申请明细-宿舍(ApplySettleDormitory)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
public interface ApplySettleDormitoryService extends IService<ApplySettleDormitory> {

    void copyByApplyId(@Param("sourceSettleId") Long sourceSettleId, @Param("targetSettleId") Long targetSettleId);
}

