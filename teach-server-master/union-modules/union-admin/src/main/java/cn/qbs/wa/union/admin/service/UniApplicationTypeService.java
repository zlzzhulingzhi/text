package cn.qbs.wa.union.admin.service;

import cn.qbs.wa.union.admin.entity.UniApplicationType;
import cn.qbs.wa.union.admin.pojo.uniapplicationtype.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 统一应用类型(UniApplicationType)表服务接口
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
public interface UniApplicationTypeService extends IService<UniApplicationType> {

    /**
     * 新增统一应用类型
     * @param params
     * @return
     */
    boolean add(UniApplicationTypeAddRequest params);

    /**
     * 分页查询统一应用类型
     * @param params
     * @return
     */
    IPage<UniApplicationTypePageResponse> page(UniApplicationTypePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    UniApplicationTypeDetailResponse detail(Serializable id);

    /**
     * 更新统一应用类型
     * @param params
     * @return
     */
    boolean update(UniApplicationTypeUpdateRequest params);

    /**
     * 删除统一应用类型
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<UniApplicationTypeTreeResponse> getTreeList(UniApplicationTypeTreeRequest request);
}

