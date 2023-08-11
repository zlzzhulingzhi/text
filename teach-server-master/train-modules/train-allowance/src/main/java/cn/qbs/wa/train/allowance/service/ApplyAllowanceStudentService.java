package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplyAllowanceStudent;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请明细-网络安全培训学员(ApplyAllowanceStudent)表服务接口
 *
 * @author makejava
 * @since 2022-11-03 19:30:25
 */
public interface ApplyAllowanceStudentService extends IService<ApplyAllowanceStudent> {

    /**
     * 新增资助资金申请明细-网络安全培训学员
     *
     * @param params
     * @return
     */
    boolean add(ApplyAllowanceStudentAddRequest params);

    /**
     * 分页查询资助资金申请明细-网络安全培训学员
     *
     * @param params
     * @return
     */
    IPage<ApplyAllowanceStudentPageResponse> page(ApplyAllowanceStudentPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ApplyAllowanceStudentDetailResponse detail(Serializable id);

    /**
     * 更新资助资金申请明细-网络安全培训学员
     *
     * @param params
     * @return
     */
    boolean update(ApplyAllowanceStudentUpdateRequest params);

    /**
     * 删除资助资金申请明细-网络安全培训学员
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    void copyByApplyId(Long sourceAllowanceId, Long targetAllowanceId);
}

