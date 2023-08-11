package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.Apply;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyRequest;
import cn.qbs.wa.train.allowance.pojo.approve.ApprovePageRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 申请表(Apply)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:05:37
 */
public interface ApplyMapper extends BaseMapper<Apply> {

    IPage<ApplyPageResponse> page(@Param("page") IPage<?> page, @Param("params") ApplyRequest applyRequest);

    /**
     * 通用单表单查询
     *
     * @param page         分页对象
     * @param applyRequest 请求参数
     */
    IPage<ApplyPageResponse> pageCommonly(IPage<?> page, @Param("params") ApplyRequest applyRequest);

    List<Long> collectOrg(@Param("applyTypes") List<String> applyTypes);

    long pageCount(@Param("params") ApprovePageRequest request);
}

