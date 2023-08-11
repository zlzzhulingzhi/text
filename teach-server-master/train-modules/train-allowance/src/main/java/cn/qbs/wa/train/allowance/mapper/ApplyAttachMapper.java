package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplyAttach;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 资助评审附件(ApplyAttach)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 18:59:29
 */
public interface ApplyAttachMapper extends BaseMapper<ApplyAttach> {

    void copyByApplyId(@Param("sourceApplyId") Long sourceApplyId, @Param("section") String section, @Param("targetApplyId") Long targetApplyId);
}

