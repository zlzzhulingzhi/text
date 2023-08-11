package cn.qbs.wa.train.screen.service;

import cn.qbs.wa.train.screen.entity.ScreenStatStudentMonthly;
import cn.qbs.wa.train.screen.pojo.screenstatstudentmonthly.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏-每月学员数量统计(ScreenStatStudentMonthly)表服务接口
 *
 * @author makejava
 * @since 2022-10-14 15:06:30
 */
public interface ScreenStatStudentMonthlyService extends IService<ScreenStatStudentMonthly> {

    /**
     * 分页查询大屏-每月学员数量统计
     *
     * @param params
     * @return
     */
    IPage<ScreenStatStudentMonthlyPageResponse> page(ScreenStatStudentMonthlyPageRequest params);

}

