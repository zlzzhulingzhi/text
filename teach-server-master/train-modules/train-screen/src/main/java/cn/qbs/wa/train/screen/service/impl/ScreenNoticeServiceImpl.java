package cn.qbs.wa.train.screen.service.impl;

import cn.qbs.wa.train.screen.mapper.ScreenNoticeMapper;
import cn.qbs.wa.train.screen.entity.ScreenNotice;
import cn.qbs.wa.train.screen.service.ScreenNoticeService;
import cn.qbs.wa.train.screen.pojo.screennotice.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 大屏-通知(ScreenNotice)表服务实现类
 *
 * @author makejava
 * @since 2022-10-14 15:30:33
 */
@Slf4j
@Service("screenNoticeService")
public class ScreenNoticeServiceImpl extends ServiceImpl<ScreenNoticeMapper, ScreenNotice> implements ScreenNoticeService {

    @Override
    public IPage<ScreenNoticePageResponse> page(ScreenNoticePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

}

