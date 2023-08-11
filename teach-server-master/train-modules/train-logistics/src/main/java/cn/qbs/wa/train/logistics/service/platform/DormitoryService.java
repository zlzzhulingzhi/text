package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.entity.Dormitory;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomPageResponse;
import cn.qbs.wa.train.logistics.pojo.classroom.ClassroomUpdateBatchRequest;
import cn.qbs.wa.train.logistics.pojo.dormitory.*;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.UseDateStateCount;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * 宿舍表(Dormitory)表服务接口
 *
 * @author makejava
 * @since 2022-10-08 17:39:59
 */
public interface DormitoryService extends IService<Dormitory> {

    /**
     * 新增宿舍表
     *
     * @param params
     * @return
     */
    boolean add(DormitoryAddRequest params);

    /**
     * 分页查询宿舍表
     *
     * @param params
     * @return
     */
    IPage<DormitoryPageResponse> page(DormitoryPageRequest params);

    List<DormitoryPageResponse> pages(DormitoryPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    DormitoryDetailResponse detail(Long id);

    /**
     * 更新宿舍表
     *
     * @param params
     * @return
     */
    boolean update(DormitoryUpdateRequest params);

    /**
     * 删除宿舍表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    boolean updateBatch(DormitoryUpdateBatchRequest params);

    List<Dormitory> listDormitory(@RequestBody DormitoryPageRequest request);

    IPage<DormitoryPageResponse> getDormitoryState(DormitoryPageRequest params);

    List<UseDateStateCount> getDormitoryCount(DormitoryPageRequest params);

}

