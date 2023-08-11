package cn.qbs.wa.teach.basic.service;

import cn.qbs.wa.teach.basic.entity.Dict;
import cn.qbs.wa.teach.basic.pojo.dict.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 字典表(Dict)表服务接口
 *
 * @author makejava
 * @since 2021-11-08 13:32:05
 */
public interface DictService extends IService<Dict> {

    /**
     * 新增字典表
     * @param params
     * @return
     */
    boolean add(DictAddRequest params);

    /**
     * 分页查询字典表
     * @param params
     * @return
     */
    IPage<DictPageResponse> page(DictPageRequest params);

    String getDictKey(DictPageRequest params);

    String getDictValue(DictPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    DictDetailResponse detail(Serializable id);

    /**
     * 更新字典表
     * @param params
     * @return
     */
    boolean update(DictUpdateRequest params);

    /**
     * 删除字典表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<TreeDictResponse>  treeDict(TreeDictRequest params);

    List<DictListResponse> childList(DictChildRequest params);

    void batchEnabled(Integer flag, List<Long> idList);
}

