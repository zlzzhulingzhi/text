package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.organization.entity.ImportEmployee;
import cn.qbs.wa.teach.organization.excel.EmployDataParseResult;
import cn.qbs.wa.teach.organization.excel.EmployeeData;
import cn.qbs.wa.teach.organization.excel.EmployeeDownloadData;
import cn.qbs.wa.teach.organization.excel.StudentDataParseResult;
import cn.qbs.wa.teach.organization.pojo.employee.EmployeeDownloadRequest;
import cn.qbs.wa.teach.organization.pojo.importemployee.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * 导入职工表(ImportEmployee)表服务接口
 *
 * @author makejava
 * @since 2021-11-16 11:29:42
 */
public interface ImportEmployeeService extends IService<ImportEmployee> {

    /**
     * 新增导入职工表
     * @param params
     * @return
     */
    boolean add(ImportEmployeeAddRequest params);

    /**
     * 分页查询导入职工表
     * @param params
     * @return
     */
    IPage<ImportEmployeePageResponse> page(ImportEmployeePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    ImportEmployeeDetailResponse detail(Serializable id);

    /**
     * 更新导入职工表
     * @param params
     * @return
     */
    boolean update(ImportEmployeeUpdateRequest params);

    /**
     * 删除导入职工表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    void saveImportData(List<EmployeeData> cachedDataList, Long orgId, String eventId, Long deptId);

    List<EmployeeDownloadData> listDownloadData(EmployeeDownloadRequest request);

    List<EmployeeDataParseResult> importPre(MultipartFile file, Long orgId);
}

