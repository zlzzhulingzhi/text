package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.UniApplicationUser;
import cn.qbs.wa.union.admin.pojo.uniapplicationuser.*;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统应用-用户】(UniApplicationUser)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:11
 */
public interface UniApplicationUserService extends IService<UniApplicationUser> {

    /**
     * 新增【系统应用-用户】
     * @param params
     * @return
     */
    boolean add(UniApplicationUserAddRequest params);

    /**
     * 分页查询【系统应用-用户】
     * @param params
     * @return
     */
    IPage<UniApplicationUserPageResponse> page(UniApplicationUserPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    UniApplicationUserDetailResponse detail(Serializable id);

    /**
     * 更新【系统应用-用户】
     * @param params
     * @return
     */
    boolean update(UniApplicationUserUpdateRequest params);

    /**
     * 删除【系统应用-用户】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<UniApplicationUserDetailResponse> getMineApplication(UniApplicationUserMineRequest request);

    List<UniApplicationUserDetailResponse> getApplicationList(IdRequest idRequest);
}

