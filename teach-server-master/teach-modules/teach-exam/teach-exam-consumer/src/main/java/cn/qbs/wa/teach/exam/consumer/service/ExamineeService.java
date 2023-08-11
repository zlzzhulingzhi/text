package cn.qbs.wa.teach.exam.consumer.service;

import cn.qbs.wa.teach.exam.common.entity.Examinee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考生表(Examinee)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeService extends IService<Examinee> {

    /**
     * 更新考生考试剩余次数
     * @param examineeId
     * @param examLimitCount
     */
    void updateRemainingTimes(Long examineeId, Integer examLimitCount);

}

