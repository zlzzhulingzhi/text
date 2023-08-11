package cn.qbs.wa.teach.exam.admin.service;


import cn.qbs.wa.teach.exam.admin.pojo.examineeliveroom.*;
import cn.qbs.wa.teach.exam.common.entity.ExamineeLiveRoom;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 考生直播房间(ExamineeLiveRoom)表服务接口
 *
 * @author makejava
 * @since 2022-01-04 11:51:43
 */
public interface ExamineeLiveRoomService extends IService<ExamineeLiveRoom> {

    /**
     * 新增考生直播房间
     * @param params
     * @return
     */
    PushOrPullShowResult live(ExamineeLiveRoomAddRequest params);

    /**
     * 分页查询考生直播房间
     * @param params
     * @return
     */
    List<ExamineeLiveRoomPageResponse> page(ExamineeLiveRoomPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    List<ExamineeLiveRoomPageResponse> detail(ExamineeLiveRoomPageRequest params);

    /**
     * 更新考生直播房间
     * @param params
     * @return
     */
    boolean update(ExamineeLiveRoomUpdateRequest params);

    /**
     * 删除考生直播房间
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);
    
}

