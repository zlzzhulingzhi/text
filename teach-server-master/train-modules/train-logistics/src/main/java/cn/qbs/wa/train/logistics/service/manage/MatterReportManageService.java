package cn.qbs.wa.train.logistics.service.manage;

import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.MatterReport;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.pojo.matter.LiteMatterReportResponse;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportPageRequest;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportResponse;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportSaveRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 报事报修(MatterReport)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-24 15:31:34
 */
public interface MatterReportManageService extends IService<MatterReport> {

    MatterReportResponse manageDetail(Long id);

    IPage<MatterReportResponse> pageMatterReport(MatterReportPageRequest params);

    List<Classroom> listMatterReportClassroom(Long orgId);


}

