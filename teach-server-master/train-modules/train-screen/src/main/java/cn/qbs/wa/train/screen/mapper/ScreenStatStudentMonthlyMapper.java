package cn.qbs.wa.train.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.screen.entity.ScreenStatStudentMonthly;
import cn.qbs.wa.train.screen.pojo.screenstatstudentmonthly.ScreenStatStudentMonthlyPageRequest;
import cn.qbs.wa.train.screen.pojo.screenstatstudentmonthly.ScreenStatStudentMonthlyPageResponse;

/**
 * 大屏-每月学员数量统计(ScreenStatStudentMonthly)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-14 15:08:47
 */
public interface ScreenStatStudentMonthlyMapper extends BaseMapper<ScreenStatStudentMonthly> {

    IPage<ScreenStatStudentMonthlyPageResponse> page(@Param("page") IPage<?> page, @Param("params") ScreenStatStudentMonthlyPageRequest params);

}

