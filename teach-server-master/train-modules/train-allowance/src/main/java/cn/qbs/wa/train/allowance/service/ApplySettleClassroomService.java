package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplySettleClassroom;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 入驻申请明细-教室(ApplySettleClassroom)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
public interface ApplySettleClassroomService extends IService<ApplySettleClassroom> {

    void copyByApplyId(Long sourceSettleId, Long targetSettleId);

}

