package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.train.allowance.entity.ApplySettleDormitory;
import cn.qbs.wa.train.allowance.mapper.ApplySettleDormitoryMapper;
import cn.qbs.wa.train.allowance.service.ApplySettleDormitoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * 入驻申请明细-宿舍(ApplySettleDormitory)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
@Slf4j
@Service("applySettleDormitoryService")
public class ApplySettleDormitoryServiceImpl extends ServiceImpl<ApplySettleDormitoryMapper, ApplySettleDormitory> implements ApplySettleDormitoryService {

    @Override
    public void copyByApplyId(Long sourceSettleId, Long targetSettleId) {
        this.baseMapper.copyByApplyId(sourceSettleId, targetSettleId);
    }

}

