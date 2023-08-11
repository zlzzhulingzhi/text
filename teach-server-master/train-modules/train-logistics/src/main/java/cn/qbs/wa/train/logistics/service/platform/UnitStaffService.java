package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.UnitStaff;
import cn.qbs.wa.train.logistics.pojo.importUnitStaff.UnitStaffDataParseResult;
import cn.qbs.wa.train.logistics.pojo.unitstaff.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * 单位职员表(UnitStaff)表服务接口
 *
 * @author makejava
 * @since 2022-09-29 09:04:01
 */
public interface UnitStaffService extends IService<UnitStaff> {

    /**
     * 新增单位职员表
     *
     * @param params
     * @return
     */
    boolean add(UnitStaffAddRequest params);

    /**
     * 分页查询单位职员表
     *
     * @param params
     * @return
     */
    IPage<UnitStaffPageResponse> page(UnitStaffPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    UnitStaffDetailResponse detail(Serializable id);

    /**
     * 更新单位职员表
     *
     * @param params
     * @return
     */
    boolean update(UnitStaffUpdateRequest params);

    boolean updateBatch(List<UnitStaffUpdateBatchRequest> params);

    /**
     * 删除单位职员表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<UnitStaffDataParseResult> importPre(MultipartFile file, Long unitId);

    List<UnitStaffAddRequest> batchAdd(List<UnitStaffExcelAddRequest> params);

}

