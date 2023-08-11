package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplySettle;
import cn.qbs.wa.train.allowance.entity.ApplySettleClassroom;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.approve.ApplySettleDetailResponse;
import cn.qbs.wa.train.allowance.pojo.approve.ApplySettlePageRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 入驻申请(ApplySettle)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
public interface ApplySettleMapper extends BaseMapper<ApplySettle> {

    ApplySettleDetailResponse selectByApplyId(Long applyId);

    IPage<ApplyPageResponse> page(@Param("page") IPage<?> page,
                                  @Param("params") ApplySettlePageRequest applySettlePageRequest);

    IPage<ApplyPageResponse> pageRecord(@Param("page") IPage<?> page,
                                  @Param("params") ApplySettlePageRequest applySettlePageRequest);

    List<ApplySettleClassroom> listClassroomSchedule(@Param("applyIds") List<Long> applyIds, @Param("classroomIds") Set<Long> classroomIds);
}

