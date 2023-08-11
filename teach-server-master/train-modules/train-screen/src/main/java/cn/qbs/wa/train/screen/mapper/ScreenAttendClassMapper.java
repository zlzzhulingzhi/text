package cn.qbs.wa.train.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.screen.entity.ScreenAttendClass;
import cn.qbs.wa.train.screen.pojo.screenattendclass.ScreenAttendClassPageRequest;
import cn.qbs.wa.train.screen.pojo.screenattendclass.ScreenAttendClassPageResponse;

/**
 * 大屏-今日培训班级(ScreenAttendClass)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-14 14:51:51
 */
public interface ScreenAttendClassMapper extends BaseMapper<ScreenAttendClass> {

    IPage<ScreenAttendClassPageResponse> page(@Param("page") IPage<?> page, @Param("params") ScreenAttendClassPageRequest params);


}

