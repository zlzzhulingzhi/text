package cn.qbs.wa.teach.course.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import cn.qbs.wa.teach.course.standard.entity.TWxReport;

/**
 * 课程-微信海报(TWxReport)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-25 11:23:33
 */
public interface TWxReportMapper extends BaseMapper<TWxReport> {

    TWxReport page(@Param("params") TWxReport params);


}

