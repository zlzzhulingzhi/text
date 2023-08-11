package cn.qbs.wa.train.allowance.service.impl;

import cn.qbs.wa.train.allowance.entity.ApplySettleClassroom;
import cn.qbs.wa.train.allowance.mapper.ApplySettleClassroomMapper;
import cn.qbs.wa.train.allowance.service.ApplySettleClassroomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 入驻申请明细-教室(ApplySettleClassroom)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
@Slf4j
@Service("applySettleClassroomService")
public class ApplySettleClassroomServiceImpl extends ServiceImpl<ApplySettleClassroomMapper, ApplySettleClassroom> implements ApplySettleClassroomService {

    @Override
    public void copyByApplyId(Long sourceSettleId, Long targetSettleId) {
        this.baseMapper.copyByApplyId(sourceSettleId, targetSettleId);
    }
    
}

