package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplyAttach;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.enums.AttachSectionEnum;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyExpertAttachAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyExpertAttachListRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 资助评审附件(ApplyAttach)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 18:59:29
 */
public interface ApplyAttachService extends IService<ApplyAttach> {

    /**
     * @param applyTypeEnum     申请类型枚举
     * @param attachSectionEnum 附件数据板块枚举
     * @param attachRequest     附件列表
     */
    List<ApplyAttachResponse> saveBatch(ApplyTypeEnum applyTypeEnum, AttachSectionEnum attachSectionEnum, ApplyAttachAddRequest attachRequest);

    /**
     * @param applyTypeEnum     申请类型枚举
     * @param attachSectionEnum 附件数据板块枚举
     * @param applyId           申请ID
     */
    List<ApplyAttachResponse> list(ApplyTypeEnum applyTypeEnum, AttachSectionEnum attachSectionEnum, Long applyId);

    List<ApplyAttachResponse> listExpertAttach(AttachSectionEnum attachSectionEnum, ApplyExpertAttachListRequest request);

    List<ApplyAttachResponse> saveBatchExpertAttach(AttachSectionEnum attachSectionEnum, ApplyExpertAttachAddRequest attachRequest);
}

