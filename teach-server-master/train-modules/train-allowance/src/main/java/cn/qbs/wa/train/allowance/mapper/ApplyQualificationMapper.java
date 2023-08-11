package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplyQualification;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.approve.ApprovePageRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资助资格申请(ApplyQualification)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 11:20:28
 */
public interface ApplyQualificationMapper extends BaseMapper<ApplyQualification> {

    List<ApplyPageResponse> pageList(@Param("pageIdx") long pageIdx, @Param("pageSize") long pageSize, @Param("params") ApprovePageRequest applyRequest);

    IPage<ApplyPageResponse> pageV2(@Param("page") IPage<?> page, @Param("params") ApprovePageRequest applyRequest);
}

