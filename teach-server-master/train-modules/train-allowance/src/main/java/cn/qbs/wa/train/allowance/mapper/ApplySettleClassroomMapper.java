package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplySettleClassroom;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageRequest;
import cn.qbs.wa.train.allowance.pojo.approve.ApplySettleClassroomDetailResponse;
import cn.qbs.wa.train.allowance.pojo.approve.ApplySettleClassroomPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 入驻申请明细-教室(ApplySettleClassroom)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
public interface ApplySettleClassroomMapper extends BaseMapper<ApplySettleClassroom> {

    IPage<ApplySettleClassroomPageResponse> page(Page<?> page, @Param("params") ApplyPageRequest applyPageRequest);

    ApplySettleClassroomDetailResponse detail(Long id);

    void copyByApplyId(@Param("sourceSettleId") Long sourceSettleId, @Param("targetSettleId") Long targetSettleId);

}

