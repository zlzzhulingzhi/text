package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.MemberVisit;
import cn.qbs.wa.train.logistics.pojo.membervisit.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 学员访问管理(MemberVisit)表服务接口
 *
 * @author makejava
 * @since 2022-12-28 16:24:20
 */
public interface MemberVisitService extends IService<MemberVisit> {

    /**
     * 新增学员访问管理
     *
     * @param params
     * @return
     */
    boolean add(MemberVisitAddRequest params);

    /**
     * 分页查询学员访问管理
     *
     * @param params
     * @return
     */
    IPage<MemberVisitPageResponse> page(MemberVisitPageRequest params);

    IPage<MemberVisitPageResponse> pageLite(MemberVisitPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    MemberVisitDetailResponse detail(Serializable id);

    /**
     * 更新学员访问管理
     *
     * @param params
     * @return
     */
    boolean update(MemberVisitUpdateRequest params);

    /**
     * 删除学员访问管理
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

