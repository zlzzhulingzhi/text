package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.entity.DormitorySchedule;
import cn.qbs.wa.train.logistics.excel.DormitoryScheduleDataParseResult;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

/**
 * 宿舍排期表(DormitorySchedule)表服务接口
 *
 * @author makejava
 * @since 2022-10-18 10:03:57
 */
public interface DormitoryScheduleService extends IService<DormitorySchedule> {

    /**
     * 新增宿舍排期表
     *
     * @param params
     * @return
     */
    List<DormitorySchedule> batchAdd(List<DormitoryScheduleDataParseResult> params) throws ParseException;

    /**
     * 分页查询宿舍排期表
     *
     * @param params
     * @return
     */
    IPage<DormitorySchedulePageResponse> page(DormitorySchedulePageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    DormitoryScheduleDetailResponse detail(Serializable id);

    /**
     * 更新宿舍排期表
     *
     * @param params
     * @return
     */
    boolean update(DormitoryScheduleUpdateRequest params);

    /**
     * 删除宿舍排期表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<DormitoryScheduleDataParseResult> importPre(MultipartFile file);

    /*List<DormitoryStateResponse> getDormitoryState(DormitoryStateRequest params);*/

}

