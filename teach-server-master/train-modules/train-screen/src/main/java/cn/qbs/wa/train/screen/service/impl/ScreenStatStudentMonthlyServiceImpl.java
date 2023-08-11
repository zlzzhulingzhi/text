package cn.qbs.wa.train.screen.service.impl;

import cn.qbs.wa.train.screen.mapper.ScreenStatStudentMonthlyMapper;
import cn.qbs.wa.train.screen.entity.ScreenStatStudentMonthly;
import cn.qbs.wa.train.screen.service.ScreenStatStudentMonthlyService;
import cn.qbs.wa.train.screen.pojo.screenstatstudentmonthly.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 大屏-每月学员数量统计(ScreenStatStudentMonthly)表服务实现类
 *
 * @author makejava
 * @since 2022-10-14 15:06:30
 */
@Slf4j
@Service("screenStatStudentMonthlyService")
public class ScreenStatStudentMonthlyServiceImpl extends ServiceImpl<ScreenStatStudentMonthlyMapper, ScreenStatStudentMonthly> implements ScreenStatStudentMonthlyService {


    @Override
    public IPage<ScreenStatStudentMonthlyPageResponse> page(ScreenStatStudentMonthlyPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

}

