package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplyAuditExpert;
import cn.qbs.wa.train.allowance.pojo.applyauditexpert.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 资助评审专家(ApplyAuditExpert)表服务接口
 *
 * @author makejava
 * @since 2023-04-04 14:19:13
 */
public interface ApplyAuditExpertService extends IService<ApplyAuditExpert> {

    /**
     * 新增资助评审专家
     * @param params
     * @return
     */
    boolean add(ApplyAuditExpertAddRequest params);


    /**
     * 获取详细信息
     * @param id
     * @return
     */
    List<ApplyAuditExpertDetailResponse> listApplyAuditExpert(ApplyAuditExpertListRequest request);

    /**
     * 更新资助评审专家
     * @param params
     * @return
     */
    boolean update(ApplyAuditExpertUpdateRequest params);

    /**
     * 删除资助评审专家
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);
    
}

