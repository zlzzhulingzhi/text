package cn.qbs.wa.train.basic.service;

import cn.qbs.wa.train.basic.entity.ClockInConfig;
import cn.qbs.wa.train.basic.pojo.clockinconfig.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 打卡配置(ClockInConfig)表服务接口
 *
 * @author makejava
 * @since 2022-12-26 15:38:20
 */
public interface ClockInConfigService extends IService<ClockInConfig> {

    /**
     * 新增打卡配置
     * @param params
     * @return
     */
    boolean add(ClockInConfigAddRequest params);

    /**
     * 分页查询打卡配置
     * @param params
     * @return
     */
    IPage<ClockInConfigPageResponse> page(ClockInConfigPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    ClockInConfigDetailResponse detail(Serializable id);

    /**
     * 更新打卡配置
     * @param params
     * @return
     */
    boolean update(ClockInConfigUpdateRequest params);

    /**
     * 删除打卡配置
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<ClockInConfigDetailResponse> configList(ClockInConfigListRequest request);
}

