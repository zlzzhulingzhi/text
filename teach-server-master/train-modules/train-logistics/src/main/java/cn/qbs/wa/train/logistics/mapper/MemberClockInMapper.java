package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.MemberClockIn;
import cn.qbs.wa.train.logistics.pojo.memberclockin.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 学员打卡记录(MemberClockIn)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-26 15:42:21
 */
public interface MemberClockInMapper extends BaseMapper<MemberClockIn> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<MemberClockIn> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<MemberClockIn> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<MemberClockIn> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<MemberClockIn> entities);

    IPage<MemberClockInPageResponse> page(@Param("page") IPage<?> page, @Param("params") MemberClockInPageRequest params);

    IPage<MemberClockInPageResponse> pages(@Param("page") IPage<?> page, @Param("params") MemberClockInPagesRequest params);

    MemberClockInDetailResponse selectDetailById(Serializable id);

    List<MemberClockInCalendarDetailResponse> calendarList(@Param("params") MemberClockInCalendarRequest params);
}

