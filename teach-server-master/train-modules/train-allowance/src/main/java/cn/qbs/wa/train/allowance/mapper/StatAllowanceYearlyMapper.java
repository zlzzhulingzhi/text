package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.StatAllowanceYearly;
import cn.qbs.wa.train.allowance.pojo.stat.StatAllowanceYearlyPageRequest;
import cn.qbs.wa.train.allowance.pojo.stat.StatAllowanceYearlyResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资助汇总统计-年度(StatAllowanceYearly)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-16 09:55:54
 */
public interface StatAllowanceYearlyMapper extends BaseMapper<StatAllowanceYearly> {

    IPage<StatAllowanceYearlyResponse> selectByCategory(@Param("page") IPage<?> page, @Param("request") StatAllowanceYearlyPageRequest request);

    void refresh();

    int replaceInsertBatch(@Param("entities") List<StatAllowanceYearly> entities);

    int deleteAll();
}
