package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplySettleDormitory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 入驻申请明细-宿舍(ApplySettleDormitory)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
public interface ApplySettleDormitoryMapper extends BaseMapper<ApplySettleDormitory> {

    void copyByApplyId(@Param("sourceSettleId") Long sourceSettleId, @Param("targetSettleId") Long targetSettleId);

}

