package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.MatterReport;
import cn.qbs.wa.train.logistics.pojo.matter.LiteMatterReportResponse;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportSaveRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 报事报修(MatterReport)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-24 15:31:34
 */
public interface MatterReportService extends IService<MatterReport> {

    MatterReport save(MatterReportSaveRequest params);

    LiteMatterReportResponse detail(Long id);

}

