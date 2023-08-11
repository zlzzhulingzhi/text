package cn.qbs.wa.train.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.screen.entity.ScreenNotice;
import cn.qbs.wa.train.screen.pojo.screennotice.ScreenNoticePageRequest;
import cn.qbs.wa.train.screen.pojo.screennotice.ScreenNoticePageResponse;

/**
 * 大屏-通知(ScreenNotice)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-14 15:30:33
 */
public interface ScreenNoticeMapper extends BaseMapper<ScreenNotice> {

    IPage<ScreenNoticePageResponse> page(@Param("page") IPage<?> page, @Param("params") ScreenNoticePageRequest params);

}

