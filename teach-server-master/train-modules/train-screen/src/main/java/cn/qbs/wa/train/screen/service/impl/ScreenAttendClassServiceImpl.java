package cn.qbs.wa.train.screen.service.impl;

import cn.qbs.wa.train.screen.mapper.ScreenAttendClassMapper;
import cn.qbs.wa.train.screen.entity.ScreenAttendClass;
import cn.qbs.wa.train.screen.service.ScreenAttendClassService;
import cn.qbs.wa.train.screen.pojo.screenattendclass.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 大屏-今日培训班级(ScreenAttendClass)表服务实现类
 *
 * @author makejava
 * @since 2022-10-14 14:49:06
 */
@Slf4j
@Service("screenAttendClassService")
public class ScreenAttendClassServiceImpl extends ServiceImpl<ScreenAttendClassMapper, ScreenAttendClass> implements ScreenAttendClassService {

    @Override
    public IPage<ScreenAttendClassPageResponse> page(ScreenAttendClassPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

}

