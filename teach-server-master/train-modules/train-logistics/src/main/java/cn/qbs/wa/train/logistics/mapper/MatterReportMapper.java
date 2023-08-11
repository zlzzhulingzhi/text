package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.MatterReport;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.pojo.matter.LiteMatterReportPageRequest;
import cn.qbs.wa.train.logistics.pojo.matter.LiteMatterReportResponse;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportPageRequest;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报事报修(MatterReport)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-24 15:31:34
 */
public interface MatterReportMapper extends BaseMapper<MatterReport> {

    LiteMatterReportResponse detail(Long id);

    IPage<LiteMatterReportResponse> page(Page<?> page, @Param("params") LiteMatterReportPageRequest params);

    MatterReportResponse manageDetail(Long id);

    IPage<MatterReportResponse> pageMatterReport(Page<?> page, @Param("params") MatterReportPageRequest params);

    List<Classroom> listMatterReportClassroom(Long orgId);

    List<Organization> listMatterReportOrg();
}

