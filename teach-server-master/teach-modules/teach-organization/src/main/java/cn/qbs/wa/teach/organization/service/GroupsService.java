package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.organization.entity.Groups;
import cn.qbs.wa.teach.organization.pojo.category.CategoryTreeRequest;
import cn.qbs.wa.teach.organization.pojo.category.CategoryTreeResponse;
import cn.qbs.wa.teach.organization.pojo.groups.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分组表(Groups)表服务接口
 *
 * @author makejava
 * @since 2022-03-28 09:27:33
 */
public interface GroupsService extends IService<Groups> {

    /**
     * 新增通用分组表
     * @param params
     * @return
     */
    boolean add(GroupsAddRequest params);

    /**
     * 分页查询通用分组表
     * @param params
     * @return
     */
    IPage<GroupsPageResponse> page(GroupsPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    GroupsDetailResponse detail(Serializable id);

    /**
     * 更新通用分组表
     * @param params
     * @return
     */
    boolean update(GroupsUpdateRequest params);

    /**
     * 删除通用分组表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<GroupsPageResponse> selectAll();

    List<GroupsDetailResponse> detailList(List<Long> idList);

    List<GroupsDetailResponse> selectListAll();

    List<GroupsTreeResponse> tree(GroupsTreeRequest params);
}

