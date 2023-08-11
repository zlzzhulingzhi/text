package cn.qbs.wa.train.allowance.mapper;

import java.io.Serializable;
import java.util.List;

import cn.qbs.wa.train.allowance.pojo.applyauditexpert.ApplyAuditExpertListRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.qbs.wa.train.allowance.entity.ApplyAuditExpert;
import cn.qbs.wa.train.allowance.pojo.applyauditexpert.ApplyAuditExpertDetailResponse;
import org.apache.ibatis.annotations.Param;

/**
 * 资助评审专家(ApplyAuditExpert)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-04 14:19:13
 */
public interface ApplyAuditExpertMapper extends BaseMapper<ApplyAuditExpert> {


    List<ApplyAuditExpertDetailResponse> listApplyAuditExpert( @Param("params")ApplyAuditExpertListRequest params);
    
}

