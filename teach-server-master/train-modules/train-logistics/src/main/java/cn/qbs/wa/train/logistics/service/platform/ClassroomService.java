package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.DormitorySchedule;
import cn.qbs.wa.train.logistics.excel.ClassroomDataParseResult;
import cn.qbs.wa.train.logistics.excel.DormitoryScheduleDataParseResult;
import cn.qbs.wa.train.logistics.pojo.classroom.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

/**
 * 教室表(Classroom)表服务接口
 *
 * @author makejava
 * @since 2022-10-11 17:30:11
 */
public interface ClassroomService extends IService<Classroom> {

    /**
     * 新增教室表
     *
     * @param params
     * @return
     */
    boolean add(ClassroomAddRequest params);

    /**
     * 分页查询教室表
     *
     * @param params
     * @return
     */
    IPage<ClassroomPageResponse> page(ClassroomPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ClassroomDetailResponse detail(Long id);

    ClassroomPriceResponse detailFee(Long id);

    /**
     * 更新教室表
     *
     * @param params
     * @return
     */
    boolean update(ClassroomUpdateRequest params);

    boolean updateBatch(ClassroomUpdateBatchRequest params);

    /**
     * 删除教室表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<Classroom> listClassroom(ClassroomPageRequest request);

    IPage<ClassroomPageResponse> classroomSchedulePage(ClassroomPageRequest params);

    IPage<ClassroomPageResponse> getClassroomState(ClassroomPageRequest params);

    List<ClassroomDataParseResult> importPre(MultipartFile file);

    List<Classroom> batchAdd(List<ClassroomDataParseResult> params);

    IPage<ClassroomSummaryResponse> getClassroomSummary(ClassroomSummaryRequest params);

    IPage<ClassroomPageResponse> getClassroomSummaryDetail(ClassroomSummaryRequest params);

    IPage<ClassroomPageResponse> getClassroomSummaryDetailV2(ClassroomSummaryRequest params);

    IPage<ClassroomPageResponse> pageInuse(ClassroomPageRequest params);

    IPage<ClassroomPageResponse> pageUnused(ClassroomPageRequest params);
}
