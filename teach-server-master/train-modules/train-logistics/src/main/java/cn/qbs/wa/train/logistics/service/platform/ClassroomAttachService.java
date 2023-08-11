package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.ClassroomAttach;
import cn.qbs.wa.train.logistics.pojo.classroomattach.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 教室附件(ClassroomAttach)表服务接口
 *
 * @author makejava
 * @since 2022-10-12 15:36:11
 */
public interface ClassroomAttachService extends IService<ClassroomAttach> {

    /**
     * 新增教室附件
     *
     * @param params
     * @return
     */
    boolean add(ClassroomAttachAddRequest params);

    /**
     * 分页查询教室附件
     *
     * @param params
     * @return
     */
    IPage<ClassroomAttachPageResponse> page(ClassroomAttachPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ClassroomAttachDetailResponse detail(Serializable id);

    /**
     * 更新教室附件
     *
     * @param params
     * @return
     */
    boolean update(ClassroomAttachUpdateRequest params);

    /**
     * 删除教室附件
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

