package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.organization.entity.WLecturer;
import cn.qbs.wa.teach.organization.pojo.wlecturer.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 插件-讲师表(WLecturer)表服务接口
 *
 * @author makejava
 * @since 2022-03-01 13:44:49
 */
public interface WLecturerService extends IService<WLecturer> {

    /**
     * 新增插件-讲师表
     * @param params
     * @return
     */
    boolean add(WLecturerAddRequest params);

    /**
     * 分页查询插件-讲师表
     * @param params
     * @return
     */
    IPage<WLecturerPageResponse> page(WLecturerPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    WLecturerDetailResponse detail(Serializable id);

    /**
     * 更新插件-讲师表
     * @param params
     * @return
     */
    boolean update(WLecturerUpdateRequest params);

    /**
     * 删除插件-讲师表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

