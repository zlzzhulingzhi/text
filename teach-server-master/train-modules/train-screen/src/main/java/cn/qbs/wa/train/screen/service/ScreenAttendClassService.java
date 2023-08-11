package cn.qbs.wa.train.screen.service;

import cn.qbs.wa.train.screen.entity.ScreenAttendClass;
import cn.qbs.wa.train.screen.pojo.screenattendclass.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏-今日培训班级(ScreenAttendClass)表服务接口
 *
 * @author makejava
 * @since 2022-10-14 14:49:05
 */
public interface ScreenAttendClassService extends IService<ScreenAttendClass> {


    /**
     * 分页查询大屏-今日培训班级
     *
     * @param params
     * @return
     */
    IPage<ScreenAttendClassPageResponse> page(ScreenAttendClassPageRequest params);



}

