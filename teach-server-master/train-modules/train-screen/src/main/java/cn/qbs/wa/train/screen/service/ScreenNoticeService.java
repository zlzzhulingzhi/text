package cn.qbs.wa.train.screen.service;

import cn.qbs.wa.train.screen.entity.ScreenNotice;
import cn.qbs.wa.train.screen.pojo.screennotice.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 大屏-通知(ScreenNotice)表服务接口
 *
 * @author makejava
 * @since 2022-10-14 15:30:33
 */
public interface ScreenNoticeService extends IService<ScreenNotice> {

    /**
     * 分页查询大屏-通知
     *
     * @param params
     * @return
     */
    IPage<ScreenNoticePageResponse> page(ScreenNoticePageRequest params);

}

